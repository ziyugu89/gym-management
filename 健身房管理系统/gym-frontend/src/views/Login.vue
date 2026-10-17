<template>
  <div class="login-index" :style="backgroundDiv">


    <div >

      <h3 class="title">
        <img src="../assets/temp.png" style="width: 40px;position: relative; top: 13px;right: 6px">
      </h3>

    <el-form :model="ruleForm" :rules="rules"
             status-icon
             ref="ruleForm"
             label-position="left"
             label-width="0px"
             class="demo-ruleForm login-page">

      <div class="title">
        <img src="../assets/logo2.png" style="width: 40px;position: relative; top: 13px;right: 6px">
        健身房管理系统
      </div>

      <el-form-item prop="username">
        <el-input type="text"
                  v-model="ruleForm.adminAccount"
                  auto-complete="off"
                  placeholder="用户名"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password"
                  v-model="ruleForm.adminPassword"
                  auto-complete="off"
                  placeholder="密码"
                  @keyup.enter.native="submitForm"
        ></el-input>
      </el-form-item>
      <div style="margin: 20px 0">
        <el-radio-group v-model="identity" size="mini">
          <el-radio label="1" >会员登录</el-radio>
          <el-radio label="2" >管理员登录</el-radio>
        </el-radio-group>
      </div>
      <el-form-item style="width:100%;">
        <el-button type="primary" style="width:100%;" @click="submitForm"  >登录</el-button>
      </el-form-item>
    </el-form>
    </div>

  </div>
</template>

<script>
import axios from "axios";
import {deleteEmployee, getAllEmployee, getMemberPassword} from "@/api/allApi";

export default {

    data(){
      return{
        backgroundDiv: {
          backgroundImage:
              "url(" + require("@/assets/back.png") + ")",
          backgroundRepeat: "no-repeat",
          backgroundSize: "100% 100%",
        },
        identity:'1',
        ruleForm: {
          adminAccount: '',
          adminPassword: ''
        },
        rules: {
          adminAccount: [{required: true, message: '请输入用户名', trigger: 'blur'}],
          adminPassword: [{required: true, message: '请输入密码', trigger: 'blur'}]
        }
      }
    },
  methods: {
    submitForm() {
      if (this.identity === "2") {
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            let _this = this
            axios.get('http://localhost:9090/getAdminPassword',{params:_this.ruleForm}).then(function (response) {
              if (response.data !=null) {
                window.localStorage.setItem('access-admin',JSON.stringify(response.data))
                _this.$router.replace({path:'/layout'})
              }
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      }
      else {
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            let _this = this
            console.log(_this)

            let memberPhone = _this.ruleForm.adminAccount
            let memberPassword = _this.ruleForm.adminPassword
            getMemberPassword({
              memberPhone:memberPhone,
              memberPassword:memberPassword
            }).then(res=>{
              if (res.data.code === 200) {
                window.localStorage.setItem('access-member',JSON.stringify(res.data))
                _this.$router.replace({path:'/memberLayout'})
              }else {
                this.$message({
                  message: '账号或密码错误',
                  type: 'error'
                });
              }
            }).catch(err=>{
              console.log(err.message)
            })

          } else {
            console.log('error submit!!');
            return false;
          }
        });
      }

    },
    resetForm(formName) {
      this.$refs.ruleForm.resetFields();
    }
  }
}

</script>

<style scoped>

.login-index {
  background: #ffffff;
  height: 842px;
  text-align: center;
  position: relative;
}

.login-page {
  -webkit-border-radius: 5px;
  border-radius: 5px;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.title {
  margin-bottom: 20px;
  color: #ee6c0f;
}



</style>
