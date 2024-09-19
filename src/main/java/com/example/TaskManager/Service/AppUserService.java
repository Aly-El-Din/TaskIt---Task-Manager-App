package com.example.TaskManager.Service;
import com.example.TaskManager.Model.Task;
import com.example.TaskManager.Model.AppUser;
import com.example.TaskManager.Repository.AppUserRepository;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import javax.swing.text.html.Option;
import java.util.Optional;


//Retrieving user details for authentication
@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    @Autowired
    private AppUserRepository userRepository;

   @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
       Optional<AppUser> user = userRepository.findByUsername(username);
       if(user.isPresent()){
           var userObj = user.get();
           return User.builder()
                   .username(userObj.getUsername())
                   .password(userObj.getPassword())
                   .build();
       }
       else{
            throw new UsernameNotFoundException(username);
       }
   }

    public AppUser getAppUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

   public List<Task> loadUserTasks(String username){
       Optional<AppUser> appUser = userRepository.findByUsername(username);
       if(appUser.isPresent()){
           return appUser.get().getTasks();
       }
       else{
           throw new UsernameNotFoundException("User not found");
       }
   }

}
