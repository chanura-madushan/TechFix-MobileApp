package com.example.techfix.data.repository;
import com.example.techfix.data.dao.UserDao;
import com.example.techfix.data.entity.User;
import com.example.techfix.util.PasswordUtils;
public class UserRepository {
 private final UserDao userDao;
 public UserRepository(UserDao userDao){this.userDao=userDao;}
 public User register(String name,String email,String password,String phone) throws Exception {
  if(userDao.getUserByEmail(email)!=null) throw new Exception("An account with this email already exists");
  User user=new User(name,email,PasswordUtils.hash(password),"customer",phone);
  long id=userDao.insertUser(user); user.setUserId((int)id); return user;
 }
 public User login(String email,String password) throws Exception {
  User user=userDao.getUserByEmail(email);
  if(user==null) throw new Exception("No account found with this email");
  if(!PasswordUtils.verify(password,user.getPassword())) throw new Exception("Incorrect password");
  return user;
 }
}