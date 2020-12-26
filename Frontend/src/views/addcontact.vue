<template>
  <div>
    <div class="multi-button">
      <button v-on:click="toContacts()">add</button>
      <button v-on:click="toContacts2()">return</button>
    </div>
    <br />
    <br />
    <div class="addcontact">
      <p>Contact name:</p>

      <input
        class="textbox"
        v-model="contactName"
        placeholder="Enter contact name"
      />
      <br />
      <p>Enter contact emails:</p>
      <textarea
        class="mail"
        v-model="email"
        placeholder="Enter the mail, In case of multiple mails,please insert ',' between them."
        cols="40"
        rows="5"
      ></textarea>
      <br />
    </div>
  </div>
</template>

<script>
import { BACKEND_URL, axios } from "../main";

export default {
  data() {
    return {
      name: "addcontact",
      email: this.$route.params.email,
      contactName: this.$route.params.username
    };
  },
  methods: {
    toContacts: async function() {
      if (this.$route.params.edit === "true") {
        console.log("Entered here!");
        await this.editContact();
        return;
      }
      let error = "";
      if (!this.email.length) error += "Email is empty!!\n";
      if (!this.contactName.length) error += "Contact name is empty!!\n";
      if (error !== "") {
        alert(error);
        return;
      }
      let confirm = null;
      const url = `${BACKEND_URL}addContact?email=${this.email}&name=${this.contactName}`;
      await axios.get(url).then(res => (confirm = res.data));
      console.log(url);
      console.log(confirm);
      if (confirm === true) {
        this.$router.push("/contacts");
        return;
      }
      alert("Invalid Contact!!");
    },
    toContacts2() {
      this.$router.push("/contacts");
    },
    async editContact() {
      alert("HERE");
      let confirm = null;
      const url = `${BACKEND_URL}editContacts?email=${this.email}&newName=${this.contactName}&oldName=${this.$route.params.username}`;
      await axios.get(url).then(res => (confirm = res.data));
      if (confirm === true) {
        this.$router.push("/contacts");
        return;
      }
      alert("Invalid Contact!!");
    }
  }
};
</script>

<style scoped>
.addcontact {
  padding: 15px;
  background: snow;
  box-shadow: 0 0 10px steelblue;
  position: relative;
  text-align: left;
}
.title {
  display: block;
  border-radius: 4px;

  padding: 0 0 5px;
  front-size: 14px;
  padding: 14px 40px;
  width: 95%;
}
.textbox {
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
  padding: calc(var(--space) / 1.125) var(--space) var(--space);
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
.mail {
  border-radius: 5px;
  padding: 60px 40px;
  width: 95%;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  font-size: 16pt;
  font-weight: bold;
}
</style>
