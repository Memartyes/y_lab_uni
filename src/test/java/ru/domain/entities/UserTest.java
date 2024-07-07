//package ru.domain.entities;
//
//import org.junit.jupiter.api.Test;
//import static org.assertj.core.api.Assertions.assertThat;
//
//class UserTest {
//
//    @Test
//    public void testUserConstructorWithNameAndPassword() {
//        String name = "Joey";
//        String password = "pw123";
//
//        User user = new User(name, password);
//
//        assertThat(user.getName()).isEqualTo(name);
//        assertThat(user.getPassword()).isEqualTo(password);
//        assertThat(user.getEmail()).isNull();
//    }
//
//    @Test
//    public void testUserConstructorWithNameEmailAndPassword() {
//        String name = "Joey";
//        String email = "joey@test.com";
//        String password = "pw123";
//
//        User user = new User(name, email, password);
//
//        assertThat(user.getName()).isEqualTo(name);
//        assertThat(user.getEmail()).isEqualTo(email);
//        assertThat(user.getPassword()).isEqualTo(password);
//    }
//
//    @Test
//    public void testDefaultConstructor() {
//        User user = new User();
//
//        assertThat(user.getName()).isNull();
//        assertThat(user.getEmail()).isNull();
//        assertThat(user.getPassword()).isNull();
//    }
//}