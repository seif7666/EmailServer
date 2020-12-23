<template>
  <div>
    <div class="multi-button">
      <button v-on:click="tomail()" >Return</button>
      <button v-on:click="tocompose()">Reply</button>
      <button v-on:click="tomail()">Delete Mail</button>
    </div>
    <br>
    <br>
    <div class="S_mail" >
      <h1 class = "mail-Head">{{ $route.params.head  }}</h1>
      <h3 class = "email" > {{email}}</h3>
      <h4 class ="mail-date">date : {{$route.params.date}}</h4>
      <h2>--------------------------------------------------------------------------------------</h2>
      <h2 class="mail-body">{{ $route.params.body }}</h2>
      <h1>--------------------------------------------------------------------------------------</h1>
      <h4>attachments:</h4>
      <select id="sel" @change="switchView($event, $event.target.selectedIndex)" >
        <option
                v-for="attachment in $route.params.attachments"
                :key="attachment.label"
                :value="attachment.value"
        >
            {{ attachment }}
        </option>
      </select>
      <button id = "dbtn" @click = "download">download</button>
      <br>
    </div>
  </div>
</template>

<script>

  export default {
    data(){
      return {
        name: "singlemail",
        email:""
      }
    },
    methods: {
      tomail: function() {
        this.$router.push("/mailbox");
      },
      tocompose: function() {
        this.$router.push({name: "compose", params: {reply:this.email}});
      },
    },
    mounted(){
      console.log("Called in SingleMail Line 38"+this.$route.params.isSent);
      if(this.$route.params.isSent === "true"){
        console.log("True");
        this.email = "To : "+this.$route.params.email;
      }
      else{
        console.log("False!");
        this.email = "From : "+this.$route.params.email;
      }
    }
  }
</script>

<style scoped>
  .S_mail {
    font-size: 150%;
    padding: 15px;
    background: snow;
    box-shadow: 0 0 10px steelblue;
    position: relative;
  }
  .mail-date{
    display: block;
    padding: 0 0 5px;
    front-size:14px;
    text-align: left;
  }
  .email{
    text-align: left;
    border-radius: 4px;
  }





  :root {
    --border-size: 0.125rem;
    --duration: 250ms;
    --ease: cubic-bezier(0.215, 0.61, 0.355, 1);
    --font-family: monospace;
    --color-primary: white;
    --color-secondary: black;
    --color-tertiary: dodgerblue;
    --shadow: rgba(0, 0, 0, 0.1);
    --space: 1rem;
  }

  * {
    box-sizing: border-box;
  }

  body {
    height: 100vh;
    margin: 0 auto;
    display: grid;
    place-items: center;
    padding: calc(var(--space) * 2);
    max-width: 700px;
  }

  .multi-button {
    display: flex;
    width: 100%;
    box-shadow: var(--shadow) 4px 4px;
  }

  .multi-button button {
    flex-grow: 1;
    cursor: pointer;
    position: relative;
    padding:
            calc(var(--space) / 1.125)
            var(--space)
            var(--space);
    background: yellow;
    font-size: 1.5rem;
    font-family: var(--font-family);
    text-shadow: var(--shadow) 2px 2px;
    transition: flex-grow var(--duration) var(--ease);
  }

  .multi-button button + button {
    border-left: var(--border-size) solid black;
    margin-left: calc(var(--border-size) * -1);
  }

  .multi-button button:hover,
  .multi-button button:focus {
    flex-grow: 2;
    color: white;
    outline: none;
    text-shadow: none;
    background: greenyellow;

  }

  .multi-button button:focus {
    outline: var(--border-size) dashed var(--color-primary);
    outline-offset: calc(var(--border-size) * -3);
  }

  .multi-button:hover button:focus:not(:hover) {
    flex-grow: 1;
    color: var(--color-secondary);
    background-color: var(--color-primary);
    outline-color: var(--color-tertiary);
  }

  .multi-button button:active {
    transform: translateY(var(--border-size));
  }
  #dbtn{
    flex-grow: 1;
    cursor: pointer;
    position: relative;
    width:100px;
    height: 40px;
    padding:
            calc(var(--space) / 1.125)
            var(--space)
            var(--space);
    background-color: #f1c40f;
  }
  #dbtn:hover,
  #dbtn:focus {
    flex-grow: 2;
    color: white;
    outline: none;
    text-shadow: none;
    background: green;

  }

  #sel{
    width:100px;
    height: 40px;
  }
</style>