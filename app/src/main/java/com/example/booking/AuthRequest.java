    package com.example.booking;

    public class AuthRequest {
        private String email;
        private String password;

        public AuthRequest(String login, String password) {
            this.email = login;
            this.password = password;
        }

        public String getLogin() {
            return email;
        }

        public void setLogin(String login) {
            this.email = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
