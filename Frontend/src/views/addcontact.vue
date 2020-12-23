<template>
  <div>
    <div class="multi-button">
      <button v-on:click="tocontacts()">add</button>
      <button v-on:click="tocontacts()">Delete</button>
    </div>
    <br>
    <br>
    <div class="addcontact">
      <p>contact name:</p>

      <input class ="textbox" v-model=$route.params.reply placeholder="Enter contact name"   />
      <p>enter his username:</p>
      <input class ="textbox" v-model="username" placeholder="Enter username" />
      <br />
      <p>or his email:</p>
      <input class ="textbox" v-model="email" placeholder="Enter email" />
      <br />
    </div>
    {{$route.params.reply}}
  </div>
</template>

<script>
export default {
  data() {
    return {
      name: "addcontact"
    }
  },
  methods: {
    tocontacts: function () {
      this.$router.push("/contacts");
    },
  }
}
</script>

<style scoped>
.addcontact {
  padding: 15px;
  background: snow;
  box-shadow: 0 0 10px steelblue;
  position: relative;
  text-align: left;
}
.title{
  display: block;
  border-radius: 4px;

  padding: 0 0 5px;
  front-size:14px;
  padding: 14px 40px;
  width: 95%;

}
.textbox{

  border-radius: 4px;
  padding: 14px 40px;
  width: 95%;

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
</style>