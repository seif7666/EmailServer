<template>
  <div class="home">
    <HelloWorld msg="Welcome to Your Vue.js + TypeScript App" />
  </div>
</template>

<script lang="ts">
  import {BACKEND_URL , axios} from "@/main";
  import { Options, Vue } from "vue-class-component";
  import HelloWorld from "@/components/HelloWorld.vue"; // @ is an alias to /src
  import FileReader from "@/components/FileReader.vue";
  import file from "@/components/file.vue";


  @Options({
    components: {
      HelloWorld , file
    },
    data(){
      return{
    };
    },
    methods:{

    },
  })
  export default class Home extends Vue{

    methods = {
      loadData(data: any){
        console.log(data)
      }
    };
  }
</script>
