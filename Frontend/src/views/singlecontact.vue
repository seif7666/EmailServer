/* eslint-disable */
<template>
  <div>
    <div class="multi-button">
      <button v-on:click="tocompose()">Send Mail</button>
      <button v-on:click="tocontact()">Delete Contact</button>
      <button v-on:click="toaddcontact()">Edit Contact</button>
      <button v-on:click="toContact2()">Return</button>
    </div>
    <br />
    <br />
    <div class="S_contact">
      <h1 class="contactname">Name :{{ $route.params.username }}</h1>
      <h3 class="email">Email: : {{ $route.params.email }}</h3>
    </div>
  </div>
</template>

<script>
import { BACKEND_URL, axios } from "../main.js";

export default {
  data() {
    return {
      name: "singlecontact"
    };
  },
  methods: {
    tocontact: async function() {
      await this.deleteContact();
      this.$router.push("/contacts");
    },
    toContact2: async function() {
      this.$router.push("/contacts");
    },
    tocompose: function() {
      console.log(this.$route.params.email);
      this.$router.push({
        name: "compose",
        params: {
          email: this.$route.params.email
        }
      });
    },
    toaddcontact: function() {
      this.$router.push({
        name: "addcontact",
        params: {
          email: this.$route.params.email,
          username: this.$route.params.username,
          edit: true
        }
      });
    },
    async deleteContact() {
      const list = [this.$route.params.username];
      const url = `${BACKEND_URL}deleteContacts`;
      await axios.post(url, list);
    }
  }
};
</script>


<style scoped>
  .S_contact {
    font-size: 150%;
    padding: 15px;
    background: snow;
    box-shadow: 0 0 10px steelblue;
    position: relative;
  }
  .username{
    display: block;
    padding: 0 0 5px;
    front-size:14px;
    text-align: left;
  }
  .email{
    text-align: left;
    border-radius: 4px;
  }
  .phonenumber{
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
  .multi-button button:focus {
    outline: var(--border-size) dashed var(--color-primary);
    outline-offset: calc(var(--border-size) * -3);
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
    background: lightgray;
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
    background: mediumpurple;
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
