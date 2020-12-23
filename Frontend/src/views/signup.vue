<template>
  <div class="signup">
    <h1>Username</h1>
    <input class = "txt" v-model="username" placeholder="Enter username" />
    <h1>Email</h1>
    <input class = "txt" v-model="email" placeholder="Enter email" />
    <h1>Password</h1>
    <input class = "txt" v-model="password" placeholder="Enter password" />
    <br />
    <h1>Renter Password</h1>
    <input class = "txt" v-model="rePassword" placeholder="Reenter password" />
    <br />
<!--    <h1>Phone number</h1>-->
<!--    <input class = "txt" v-model="phoneNumber" placeholder="enter phone number" />-->
<!--    <br />-->
    <h1> birthday</h1>
    <input class = "txt" type ="date"  v-model="birthday" placeholder="enter birthday" />
    <br />
    <br />
    <button  class="btn" @click="signUp">
        Sign Up
    </button>

    <br />
    <h4>Return to login screen</h4>
    <router-link to = "/login">
      <button  class="btn">
        Log In
      </button>
    </router-link>
  </div>
</template>
<script>
import {BACKEND_URL , axios} from "../main.ts";
export default {
  data(){
    return{
      username:"",
      email:"",
      password:"",
      rePassword: "",
      phoneNumber:"",
      birthday:"",
    }
  },
  methods:{
    /**
     * First we check that password and email is valid
     */
     signUp: async function() {
       alert("HERE");
      let errorMessage = "";
      if(this.password !== this.rePassword){
        errorMessage += "Passwords don't match!\n";
      }
      if(!this.username.length || !this.email.length || !this.password.length){
        errorMessage += "username , email and password fields cannot be empty!";
      }
      if(errorMessage.length) {
        alert(errorMessage);
        return;
      }
      alert("After return");
      const toBeSent = {
        email:this.email,
        userName:this.username,
        password:this.password,
        birthday:this.birthday
      };
      let confirm = ""; ///Confirm that client is valid.
      const url = `${BACKEND_URL}signUp`;
      await axios.post(url , toBeSent).then(res=>confirm = res.data + "");
      if(confirm !== 'true') {
        alert("Email is already there !");
        return;
      }
      await this.$router.push("/mailbox");
    }
  }
};
</script>

<style scoped lang="scss">
  .signup{
    padding: 15px;
    background: slategray;
    box-shadow: 0 0 10px steelblue;
    position: relative;
  }
  .txt{
    padding: 14px 30px;
    font-size: 16pt;

  }
  .btn {
    width: 14%;
    height: 20%;
    padding: 14px 40px;
    border-color: #f1c40f;
    font-size: 20pt;
    background-image: -webkit-linear-gradient(
                    45deg,
                    #f1c40f 50%,
                    transparent 50%
    );
    background-image: linear-gradient(45deg, #f1c40f 50%, transparent 50%);
    background-position: 100%;
    background-size: 400%;
    -webkit-transition: background 300ms ease-in-out;
    transition: background 300ms ease-in-out;
  }
  .btn:hover {
    background-position: 0;
  }
</style>