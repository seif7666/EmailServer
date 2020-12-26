<template>
  <div class="home">
    <HelloWorld msg="Welcome to Your Vue.js + TypeScript App" />
  </div>
</template>

<script>
import HelloWorld from "../components/HelloWorld.vue"; // @ is an alias to /src
import file from "../components/file.vue";

export default {
  components: {
    HelloWorld,
    file
  },

}
</script>
