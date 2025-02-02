package easy.market.service;

import easy.market.exception.UsernameAlreadyExistsException;
import easy.market.request.JoinRequest;
import easy.market.entity.User;
import easy.market.exception.InvalidPasswordException;
import easy.market.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void join(JoinRequest joinRequest) {
        if(!joinRequest.getPassword().equals(joinRequest.getPasswordMatch())){
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.", "password");
        }
        try {
            joinRequest.setPassword(passwordEncoder.encode(joinRequest.getPassword()));
            userRepository.save(User.joinUser(joinRequest));
        }
        catch (DataIntegrityViolationException e){
            throw new UsernameAlreadyExistsException("이미 존재하는 아이디 입니다.", "username");
        }
    }
}
