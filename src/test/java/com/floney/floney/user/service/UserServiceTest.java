package com.floney.floney.user.service;

import static com.floney.floney.common.constant.Status.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.floney.floney.book.repository.BookUserRepository;
import com.floney.floney.common.dto.Token;
import com.floney.floney.common.exception.user.PasswordSameException;
import com.floney.floney.common.exception.user.SignoutOtherReasonEmptyException;
import com.floney.floney.common.exception.user.UserFoundException;
import com.floney.floney.common.exception.user.UserNotFoundException;
import com.floney.floney.common.util.JwtProvider;
import com.floney.floney.common.util.MailProvider;
import com.floney.floney.common.util.RedisProvider;
import com.floney.floney.fixture.BookFixture;
import com.floney.floney.fixture.UserFixture;
import com.floney.floney.user.dto.constant.SignoutType;
import com.floney.floney.user.dto.request.LoginRequest;
import com.floney.floney.user.dto.request.SignoutRequest;
import com.floney.floney.user.dto.request.SignupRequest;
import com.floney.floney.user.dto.response.MyPageResponse;
import com.floney.floney.user.dto.response.UserResponse;
import com.floney.floney.user.dto.security.CustomUserDetails;
import com.floney.floney.user.entity.User;
import com.floney.floney.user.repository.SignoutReasonRepository;
import com.floney.floney.user.repository.UserRepository;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private BookUserRepository bookUserRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MailProvider mailProvider;
    @Mock
    private RedisProvider redisProvider;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private SignoutReasonRepository signoutReasonRepository;

    @Test
    @DisplayName("회원가입에 성공한다")
    void signup_success() {
        // given
        User user = UserFixture.getUser();
        SignupRequest signupRequest = SignupRequest.builder()
            .email(user.getEmail())
            .password(user.getPassword())
            .nickname(user.getNickname())
            .build();

        given(userRepository.save(any(User.class))).willReturn(null);

        // when
        userService.signup(signupRequest);

        // then
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("회원가입에 실패한다 - 이미 가입된 회원")
    void signup_fail_throws_userFoundException1() {
        // given
        User user = UserFixture.getUser();
        SignupRequest signupRequest = SignupRequest.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .build();
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> userService.signup(signupRequest)).isInstanceOf(UserFoundException.class);
    }

    @Test
    @DisplayName("로그인에 성공한다")
    void login_success() {
        // given
        User user = UserFixture.createUser();

        LoginRequest request = LoginRequest.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(jwtProvider.generateToken(anyString())).willReturn(new Token("accessToken", "refreshToken"));

        // when & then
        assertThatNoException().isThrownBy(() -> userService.login(request));
        assertThat(user.isInactive()).isFalse();
        assertThat(user.getLastLoginTime()).isNotNull();
    }

    @Test
    @DisplayName("로그인에 실패한다 - 존재하지 않는 회원")
    void login_fail_throws_userNotFoundException() {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("fail@fail.com")
                .password("fail")
                .build();

        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.login(request)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("로그인에 실패한다 - 일치하지 않는 비밀번호")
    void login_fail_throws_badCredentialException() {
        // given
        User user = UserFixture.getUser();

        LoginRequest request = LoginRequest.builder()
                .email(user.getEmail())
                .password("fail")
                .build();

        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.login(request)).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("회원탈퇴에 성공한다")
    void signout_success() {
        // given
        User user = UserFixture.createUser();
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        SignoutRequest request = new SignoutRequest(SignoutType.EXPENSIVE, null);

        // when
        userService.signout(user.getEmail(), request);

        // then
        // TODO: 탈퇴시 데이터 삭제로 수정
        assertThat(user.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("회원탈퇴에 실패한다 - 존재하지 않는 회원")
    void signout_fail_throws_usernameNotFoundException() {
        // given
        User user = UserFixture.createUser();
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        SignoutRequest request = new SignoutRequest(SignoutType.EXPENSIVE, null);

        // when & then
        assertThatThrownBy(() -> userService.signout(user.getEmail(), request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("회원탈퇴에 실패한다 - 비어있는 기타 탈퇴 사유")
    void signout_fail_throws_signoutOtherReasonEmptyException(final String value) {
        // given
        User user = UserFixture.createUser();
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        SignoutRequest request = new SignoutRequest(SignoutType.OTHER, value);

        // when & then
        assertThatThrownBy(() -> userService.signout(user.getEmail(), request))
                .isInstanceOf(SignoutOtherReasonEmptyException.class);
    }

    @Test
    @DisplayName("회원정보 얻기에 성공한다")
    void getUserInfo_success() {
        // given
        User user = UserFixture.getUser();
        given(bookUserRepository.findMyBookInfos(user)).willReturn(Collections.singletonList(BookFixture.myBookInfo()));

        // when & then
        assertThat(userService.getUserInfo(CustomUserDetails.of(user)))
                .isEqualTo(MyPageResponse.from(UserResponse.from(user), Collections.singletonList(BookFixture.myBookInfo())));
    }

    @Test
    @DisplayName("이메일 인증코드를 올바르게 생성하는 데 성공한다")
    void generateEmailAuthenticationCode_success() {
        // given
        int codeLength = 6;

        // when
        String code = userService.sendEmailAuthMail("email");

        // then
        assertThat(code.length()).isEqualTo(codeLength);
    }

    @Test
    @DisplayName("새 비밀번호를 올바르게 생성하는 데 성공한다")
    void generateNewPassword_success() {
        // given
        int passwordLength = 50;

        // when
        String newPassword = userService.sendPasswordFindEmail("email");

        // then
        assertThat(newPassword.length()).isEqualTo(passwordLength);
    }

    @Test
    @DisplayName("현재 비밀번호와 같은 비밀번호로 변경 시도 시 실패한다")
    void updatePassword_fail_samePasswordException() {
        // given
        final String password = "1234567890";
        final User user = User.builder().password(password).build();

        given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.updatePassword(password, user))
                .isInstanceOf(PasswordSameException.class);
    }
}
