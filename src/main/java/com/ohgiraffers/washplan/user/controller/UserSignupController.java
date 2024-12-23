    package com.ohgiraffers.washplan.user.controller;

    import com.ohgiraffers.washplan.user.model.dao.UserMapper;
    import com.ohgiraffers.washplan.user.model.dto.UserDTO;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;

    @Controller
    @RequestMapping("signup")
    public class UserSignupController {

        private final UserMapper userMapper;
        private final BCryptPasswordEncoder passwordEncoder;

        public UserSignupController(UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
            this.userMapper = userMapper;
            this.passwordEncoder = passwordEncoder;
        }

        @PostMapping("/submit")
        public String registerUser(@RequestParam("userId") String userId,
                                   @RequestParam("password") String password,
                                   @RequestParam("email") String email,
                                   Model model) {
            try {
                // 비밀번호 해싱
                String hashedPassword = passwordEncoder.encode(password);

                UserDTO newUser = new UserDTO();
                newUser.setUserId(userId);
                newUser.setPassword(hashedPassword); // 해싱된 비밀번호 설정
                newUser.setEmail(email);
                newUser.setUserStatus("활성"); // 기본 상태로 설정

                userMapper.insertUser(newUser);

                return "redirect:/login"; // 회원가입 완료 후 로그인 페이지로 리다이렉트
            } catch (Exception e) {
                model.addAttribute("errorMessage", "회원가입 중 오류가 발생했습니다.");
                return "signup/signup";
            }
        }
    }