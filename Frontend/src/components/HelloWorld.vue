<template>
  <div class="hello">
    <div class="bg"></div>
    <div class="bg bg2"></div>
    <div class="bg bg3"></div>
    <div class="con">
      <h1>Welcome to Mail Server</h1>
      <router-link to="/signup">
        <button class="btn1">
          signup
        </button>
      </router-link>
      <router-link to="/login">
        <button class="btn1">
          login
        </button>
      </router-link>
    </div>
  </div>
</template>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped >
.btn1 {
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
.btn1:hover {
  background-position: 0;
}
html {
  height: 100%;
}

body {
  margin: 0;
}
.con {
  padding: 10vmin;
}
</style>
