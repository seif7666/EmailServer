/* eslint-disable */
<template>
  <div class="login">
    <h1>Email</h1>
    <input class="txt" type="email" v-model="username" placeholder="Enter email" />
    <h2>Password</h2>
    <input
      class="txt"
      v-model="password"
      type="password"
      placeholder="Enter password"
    />
    <br />
    <br />
    <button v-on:click="tomail" class="btn">
      Login
    </button>
    <br />
    <h4>Don't have an account</h4>
    <router-link to="/signUp">
      <button class="btn">
        Sign Up
      </button>
    </router-link>

    <router-view></router-view>
    <br />
  </div>
</template>

<script>
import { BACKEND_URL, axios } from "../main.js";
export default {
  data() {
    return {
      username: "",
      password: ""
    };
  },
  methods: {
    /**
     * Send to back
     * username, password.
     * Receive confirmation , if received  , switch to mailbox.vue
     */
    tomail: async function() {
      //No need to create a json here, as we have only parameters.
      const url = `${BACKEND_URL}login?username=${this.username}&password=${this.password}`;
      //Now we send data
      let confirmed = false;
      console.log(url);
      await axios.get(url).then(res => {
        console.log(res);
        confirmed = res.data + "";
      });
      //Now we wait for response.
      console.log(confirmed);
      if (confirmed === "true") {
        console.log("Entered here!");
        this.$router.push("/mailbox");
      }
      else
        alert("Invalid mail or password!");
    }
  }
};
</script>

<style scoped>
.login {
  padding: 15px;
  background: steelblue;
  box-shadow: 0 0 10px steelblue;
  position: relative;
}
.txt {
  padding: 14px 30px;
  font-size: 16pt;
  font-family: Andalus;
  font-style: italic;
  font-weight: bold;
}

.btn {
  min-width: 300px;
  min-height: 60px;
  font-family: "Nunito", sans-serif;
  font-size: 22px;
  text-transform: uppercase;
  letter-spacing: 1.3px;
  font-weight: 700;
  color: #313133;
  background: #4fd1c5;
  background: linear-gradient(
    90deg,
    rgba(129, 230, 217, 1) 0%,
    rgba(79, 209, 197, 1) 100%
  );
  border: none;
  border-radius: 1000px;
  box-shadow: 12px 12px 24px rgba(79, 209, 197, 0.64);
  transition: all 0.3s ease-in-out 0s;
  cursor: pointer;
  outline: none;
  position: relative;
  padding: 10px;
}

.btn::before {
  content: "";
  border-radius: 1000px;
  min-width: calc(300px + 12px);
  min-height: calc(60px + 12px);
  border: 6px solid #00ffcb;
  box-shadow: 0 0 60px rgba(0, 255, 203, 0.64);
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  opacity: 0;
  transition: all 0.3s ease-in-out 0s;
}

.btn:hover,
.btn:focus {
  color: #313133;
  transform: translateY(-6px);
}

button:hover::before,
button:focus::before {
  opacity: 1;
}

button::after {
  content: "";
  width: 30px;
  height: 30px;
  border-radius: 100%;
  border: 6px solid #00ffcb;
  position: absolute;
  z-index: -1;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: ring 1.5s infinite;
}

button:hover::after,
.button:focus::after {
  animation: none;
  display: none;
}

@keyframes ring {
  0% {
    width: 30px;
    height: 30px;
    opacity: 1;
  }
  100% {
    width: 300px;
    height: 300px;
    opacity: 0;
  }
}
</style>
