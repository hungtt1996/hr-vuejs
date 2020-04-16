<template>
    <div>
        <el-form
                class="login-form"
                v-loading="loading"
                element-loading-text="logging in..."
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.8)"
                :model="loginForm"
                :rules="loginRules"
                ref="loginForm">
            <h3 class="login-title">HR Management System - log in</h3>
            <el-form-item prop="username">
                <el-input size="normal" type="text" v-model="loginForm.username" auto-complete="off"
                          placeholder="please enter user name"></el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input size="normal" type="password" v-model="loginForm.password" auto-complete="off"
                          placeholder="Please enter the password" @keydown.enter.native="submitForm('loginForm')"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button size="normal" class="login-btn" type="primary" @click="submitForm('loginForm')">log in
                </el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            return {
                loginForm: {
                    username: 'admin',
                    password: '123'
                },
                loginRules: {
                    username: [{required: true, message: 'please enter user name', trigger: 'blur'}],
                    password: [{required: true, message: 'Please enter the password', trigger: 'blur'}]
                },
                loading: false
            }
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        this.postKeyValueRequest('/doLogin', this.loginForm).then(resp => {
                            this.loading = false;
                            if (resp) {
                                this.$store.commit('initCurrentUser', resp.obj);
                                window.sessionStorage.setItem("user", JSON.stringify(resp.obj));
                                // If you need to jump to the target address when logging in，Then directly log in to this address after successful login，Otherwise to home page
                                let redirect = this.$route.query.redirect;
                                this.$router.replace((redirect == '/' || redirect == undefined) ? '/home' : redirect);
                            }
                        });
                    } else {
                        this.$message.error('Form validation error');
                        return false;
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .login-form {
        border-radius: 15px;
        background-clip: padding-box;
        margin: 160px auto;
        width: 350px;
        padding: 10px 35px;
        background: #fff;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 25px #cac6c6;
    }

    .login-title {
        margin: 20px auto 30px auto;
        text-align: center;
        color: #505458;
    }

    .login-btn {
        width: 100%;
        margin-top: 10px;
    }
</style>
