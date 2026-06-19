package com.celebrate.service;

import com.celebrate.dto.input.UserInput;
import com.celebrate.dto.response.*;
import com.celebrate.entity.*;
import com.celebrate.exception.*;
import com.celebrate.repository.*;
import com.celebrate.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final RiderRepository riderRepository;
    private final StaffRepository staffRepository;
    private final RestaurantRepository restaurantRepository;
    private final OtpRepository otpRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthDataResponse login(String appleId, String email, String password,
                                   String type, String name, String notificationToken,
                                   Boolean isActive) {
        return switch (type) {
            case "default" -> loginWithEmailPassword(email, password, notificationToken);
            case "apple" -> loginWithApple(appleId, email, name, notificationToken);
            case "social" -> loginWithSocial(email, name, notificationToken, false);
            default -> throw new BadRequestException("Unknown login type: " + type);
        };
    }

    private AuthDataResponse loginWithEmailPassword(String email, String password, String notificationToken) {
        if (email == null || password == null) {
            throw new BadRequestException("Email and password are required.");
        }
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No account found with this email."));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ForbiddenException("Your account has been deactivated.");
        }
        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Incorrect password.");
        }

        user.setLastLogin(LocalDateTime.now());
        if (notificationToken != null) user.setNotificationToken(notificationToken);
        userRepository.save(user);

        String token = jwtProvider.generateToken(user.getId(), user.getEmail(), "USER");
        return buildAuthDataResponse(user, token, false);
    }

    private AuthDataResponse loginWithApple(String appleId, String email, String name, String notificationToken) {
        if (appleId == null) throw new BadRequestException("Apple ID is required.");

        UserEntity user = userRepository.findByAppleId(appleId).orElse(null);
        boolean isNewUser = false;

        if (user == null) {
            if (email != null) {
                user = userRepository.findByEmail(email).orElse(null);
            }
            if (user == null) {
                user = UserEntity.builder()
                        .name(name != null ? name : "Apple User")
                        .email(email != null ? email : appleId + "@apple.placeholder")
                        .appleId(appleId)
                        .userType("USER")
                        .isActive(true)
                        .emailIsVerified(true)
                        .phoneIsVerified(false)
                        .isOrderNotification(true)
                        .isOfferNotification(true)
                        .notificationToken(notificationToken)
                        .build();
                isNewUser = true;
            } else {
                user.setAppleId(appleId);
            }
        }

        user.setLastLogin(LocalDateTime.now());
        if (notificationToken != null) user.setNotificationToken(notificationToken);
        userRepository.save(user);

        String token = jwtProvider.generateToken(user.getId(), user.getEmail(), "USER");
        return buildAuthDataResponse(user, token, isNewUser);
    }

    private AuthDataResponse loginWithSocial(String email, String name, String notificationToken, boolean isNewUser) {
        UserEntity user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = UserEntity.builder()
                    .name(name != null ? name : "Social User")
                    .email(email)
                    .userType("USER")
                    .isActive(true)
                    .emailIsVerified(true)
                    .phoneIsVerified(false)
                    .isOrderNotification(true)
                    .isOfferNotification(true)
                    .notificationToken(notificationToken)
                    .build();
            isNewUser = true;
        }

        user.setLastLogin(LocalDateTime.now());
        if (notificationToken != null) user.setNotificationToken(notificationToken);
        userRepository.save(user);

        String token = jwtProvider.generateToken(user.getId(), user.getEmail(), "USER");
        return buildAuthDataResponse(user, token, isNewUser);
    }

    @Transactional
    public AuthDataResponse loginWithSocialToken(String type, String idToken, String notificationToken) {
        // In a real scenario, verify idToken with Google/Facebook etc.
        // For now, treat email extracted from idToken as social login
        // The frontend sends a decoded email after verifying on its end
        throw new BadRequestException("Social token verification not yet implemented. Use loginWithSocial.");
    }

    @Transactional
    public AuthDataResponse loginPasswordless(String email, String otp, String notificationToken) {
        OtpEntity otpEntity = otpRepository
                .findTopByEmailAndIsUsedFalseOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new BadRequestException("No active OTP found for this email."));

        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP has expired.");
        }
        if (!otpEntity.getOtp().equals(otp)) {
            throw new BadRequestException("Invalid OTP.");
        }

        otpEntity.setIsUsed(true);
        otpRepository.save(otpEntity);

        UserEntity user = userRepository.findByEmail(email).orElse(null);
        boolean isNewUser = false;
        if (user == null) {
            user = UserEntity.builder()
                    .name("User")
                    .email(email)
                    .userType("USER")
                    .isActive(true)
                    .emailIsVerified(true)
                    .phoneIsVerified(false)
                    .isOrderNotification(true)
                    .isOfferNotification(true)
                    .notificationToken(notificationToken)
                    .build();
            isNewUser = true;
        }
        user.setLastLogin(LocalDateTime.now());
        if (notificationToken != null) user.setNotificationToken(notificationToken);
        userRepository.save(user);

        String token = jwtProvider.generateToken(user.getId(), user.getEmail(), "USER");
        return buildAuthDataResponse(user, token, isNewUser);
    }

    @Transactional
    public AuthDataResponse createUser(UserInput input) {
        if (input.getEmail() != null && userRepository.existsByEmail(input.getEmail())) {
            throw new ConflictException("An account with this email already exists.");
        }
        if (input.getPhone() != null && userRepository.existsByPhone(input.getPhone())) {
            throw new ConflictException("An account with this phone number already exists.");
        }

        UserEntity user = UserEntity.builder()
                .name(input.getName() != null ? input.getName() : "User")
                .email(input.getEmail())
                .phone(input.getPhone())
                .appleId(input.getAppleId())
                .userType("USER")
                .isActive(true)
                .emailIsVerified(input.getEmailIsVerified() != null ? input.getEmailIsVerified() : false)
                .phoneIsVerified(input.getPhoneIsVerified() != null ? input.getPhoneIsVerified() : false)
                .password(input.getPassword() != null ? passwordEncoder.encode(input.getPassword()) : null)
                .notificationToken(input.getNotificationToken())
                .isOrderNotification(true)
                .isOfferNotification(true)
                .build();

        userRepository.save(user);
        String token = jwtProvider.generateToken(user.getId(), user.getEmail(), "USER");
        return buildAuthDataResponse(user, token, true);
    }

    @Transactional
    public OwnerAuthDataResponse ownerLogin(String email, String password) {
        OwnerEntity owner = ownerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No account found with this email."));

        if (!Boolean.TRUE.equals(owner.getIsActive())) {
            throw new ForbiddenException("This account has been deactivated.");
        }
        if (!passwordEncoder.matches(password, owner.getPassword())) {
            throw new UnauthorizedException("Incorrect password.");
        }

        String token = jwtProvider.generateToken(owner.getId(), owner.getEmail(), owner.getUserType());

        List<RestaurantEntity> restaurants = restaurantRepository.findByOwnerId(owner.getId());
        List<RestaurantResponse> restaurantResponses = restaurants.stream()
                .map(r -> RestaurantResponse.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .image(r.getImage())
                        .build())
                .toList();

        return OwnerAuthDataResponse.builder()
                .userId(owner.getId())
                .token(token)
                .tokenExpiration(jwtProvider.getExpirationSeconds())
                .email(owner.getEmail())
                .userType(owner.getUserType())
                .restaurants(restaurantResponses)
                .permissions(List.of())
                .pushToken(owner.getPushToken())
                .image(owner.getImage())
                .name(owner.getName())
                .build();
    }

    @Transactional
    public OwnerAuthDataResponse adminLogin(String email, String password) {
        OwnerEntity admin = ownerRepository.findByEmailAndUserType(email, "ADMIN")
                .orElseThrow(() -> new UnauthorizedException("Invalid admin credentials."));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new UnauthorizedException("Invalid admin credentials.");
        }

        String token = jwtProvider.generateToken(admin.getId(), admin.getEmail(), "ADMIN");

        return OwnerAuthDataResponse.builder()
                .userId(admin.getId())
                .token(token)
                .tokenExpiration(jwtProvider.getExpirationSeconds())
                .email(admin.getEmail())
                .userType("ADMIN")
                .restaurants(List.of())
                .permissions(List.of())
                .pushToken(admin.getPushToken())
                .image(admin.getImage())
                .name(admin.getName())
                .build();
    }

    @Transactional
    public AuthDataResponse riderLogin(String username, String password, String notificationToken, String timeZone) {
        RiderEntity rider = riderRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials."));

        if (!Boolean.TRUE.equals(rider.getIsActive())) {
            throw new ForbiddenException("Your account has been deactivated.");
        }
        if (!passwordEncoder.matches(password, rider.getPassword())) {
            throw new UnauthorizedException("Invalid credentials.");
        }

        if (notificationToken != null) rider.setNotificationToken(notificationToken);
        if (timeZone != null) rider.setTimeZone(timeZone);
        riderRepository.save(rider);

        String token = jwtProvider.generateToken(rider.getId(), rider.getEmail() != null ? rider.getEmail() : rider.getUsername(), "RIDER");

        return AuthDataResponse.builder()
                .userId(rider.getId())
                .token(token)
                .tokenExpiration(jwtProvider.getExpirationSeconds())
                .name(rider.getName())
                .phone(rider.getPhone())
                .isActive(rider.getIsActive())
                .build();
    }

    public RestaurantAuthResponse restaurantLogin(String username, String password, String notificationToken) {
        RestaurantEntity restaurant = restaurantRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials."));

        if (!Boolean.TRUE.equals(restaurant.getIsActive())) {
            throw new ForbiddenException("This restaurant account is inactive.");
        }
        if (restaurant.getPassword() == null || !passwordEncoder.matches(password, restaurant.getPassword())) {
            throw new UnauthorizedException("Invalid credentials.");
        }

        if (notificationToken != null) {
            restaurant.setNotificationToken(notificationToken);
            restaurantRepository.save(restaurant);
        }

        String token = jwtProvider.generateToken(restaurant.getOwner() != null ? restaurant.getOwner().getId() : restaurant.getId(),
                username, "OWNER");

        return RestaurantAuthResponse.builder()
                .token(token)
                .restaurantId(restaurant.getId())
                .build();
    }

    @Transactional
    public OtpResultResponse sendOtpToEmail(String email) {
        String otp = generateOtp();
        OtpEntity otpEntity = OtpEntity.builder()
                .email(email)
                .otp(otp)
                .isUsed(false)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();
        otpRepository.save(otpEntity);
        // TODO: send via email service (SMTP/SendGrid)
        System.out.println("OTP for " + email + ": " + otp);
        return new OtpResultResponse(true);
    }

    @Transactional
    public OtpResultResponse sendOtpToPhone(String phone) {
        String otp = generateOtp();
        OtpEntity otpEntity = OtpEntity.builder()
                .phone(phone)
                .otp(otp)
                .isUsed(false)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();
        otpRepository.save(otpEntity);
        // TODO: send via Twilio/SMS service
        System.out.println("OTP for " + phone + ": " + otp);
        return new OtpResultResponse(true);
    }

    public VerifyOtpResultResponse verifyOtp(String otp, String email, String phone) {
        OtpEntity otpEntity;
        if (email != null) {
            otpEntity = otpRepository.findTopByEmailAndIsUsedFalseOrderByCreatedAtDesc(email)
                    .orElseThrow(() -> new BadRequestException("No active OTP for this email."));
        } else if (phone != null) {
            otpEntity = otpRepository.findTopByPhoneAndIsUsedFalseOrderByCreatedAtDesc(phone)
                    .orElseThrow(() -> new BadRequestException("No active OTP for this phone."));
        } else {
            throw new BadRequestException("Email or phone is required.");
        }

        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP has expired.");
        }
        if (!otpEntity.getOtp().equals(otp)) {
            return new VerifyOtpResultResponse(false);
        }

        otpEntity.setIsUsed(true);
        otpRepository.save(otpEntity);
        return new VerifyOtpResultResponse(true);
    }

    public ForgotPasswordResponse forgotPassword(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No account found with this email."));
        String otp = generateOtp();
        OtpEntity otpEntity = OtpEntity.builder()
                .email(email)
                .otp(otp)
                .isUsed(false)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();
        otpRepository.save(otpEntity);
        // TODO: send password reset email
        System.out.println("Password reset OTP for " + email + ": " + otp);
        return new ForgotPasswordResponse(true);
    }

    @Transactional
    public ForgotPasswordResponse resetPassword(String password, String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No account found with this email."));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return new ForgotPasswordResponse(true);
    }

    @Transactional
    public boolean vendorResetPassword(String oldPassword, String newPassword) {
        String ownerId = com.celebrate.security.SecurityUtil.getCurrentUserId();
        OwnerEntity owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Owner", ownerId));
        if (!passwordEncoder.matches(oldPassword, owner.getPassword())) {
            throw new BadRequestException("Current password is incorrect.");
        }
        owner.setPassword(passwordEncoder.encode(newPassword));
        ownerRepository.save(owner);
        return true;
    }

    @Transactional
    public boolean changePassword(String oldPassword, String newPassword) {
        String userId = com.celebrate.security.SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Current password is incorrect.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    private AuthDataResponse buildAuthDataResponse(UserEntity user, String token, boolean isNewUser) {
        return AuthDataResponse.builder()
                .userId(user.getId())
                .token(token)
                .tokenExpiration(jwtProvider.getExpirationSeconds())
                .name(user.getName())
                .phone(user.getPhone())
                .phoneIsVerified(user.getPhoneIsVerified())
                .email(user.getEmail())
                .emailIsVerified(user.getEmailIsVerified())
                .isNewUser(isNewUser)
                .isActive(Boolean.TRUE.equals(user.getIsActive()))
                .build();
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}
