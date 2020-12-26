/* eslint-disable */
<template>
  <div class="contacts">
    <div class="multi-button">
      <button v-on:click="toaddcontact()">Add Contact</button>

      <button @click="deleteContacts">Delete</button>
      <button v-on:click="tomail()">back</button>
      <button v-on:click="tolog()">logout</button>
    </div>

    <br />
    <br />
    <div class="search">
      <label class="textl">Search for </label>
      <input v-model="filterName" />
      <label class="textl"> in </label>

      <select id="filter" @change="onChangeFilter($event)">
        <option value="contactname">contact name</option>
        <option value="emails">emails</option>
      </select>
      <br />
      <br />
      <button class="btn" @click="filterMails">search</button>
    </div>
    <br />
    <div>
      <contact
        v-for="contact in contacts"
        v-bind:key="contact.id"
        v-bind:id="contact.id"
        v-bind:username="contact.username"
        v-bind:email="contact.email"
        @getTheContact="contactSelect"
        @getCheckedContact="checkContact"
      />
    </div>
  </div>
</template>

<script>
// import jsonContacts from "@/json/jsonContacts.json";
import { BACKEND_URL, axios } from "../main";
import contact from "@/components/contact";
export default {
  data() {
    return {
      contacts: [],
      selectedContacts: [], //List Of mails
      index: 0,
      valuesOfFilter: ["name", "emails"],
      filterName: ""
    };
  },
  name: "contacts",
  components: { contact },
  methods: {
    toaddcontact() {
      this.$router.push("/addcontact");
    },
    tomail: function() {
      this.$router.push("/mailbox");
    },
    tolog: function() {
      this.$router.push("/login");
    },
    contactSelect(contact) {
      console.log(contact);
    },
    onChangeFilter(event) {
      this.index = event.target.options.selectedIndex;
      console.log(this.index);
    },
    checkContact(id, marker) {
      console.log("In check Contact : contacts.vue");
      console.log(typeof marker);
      console.log(marker);
      if (marker) {
        console.log("TRUE");
        this.selectedContacts.push(id);
      } else {
        const index = this.selectedContacts.indexOf(id);
        if (index > -1) {
          this.selectedContacts.splice(index, 1);
        }
      }
      // console.table(this.selectedContacts)
    },
    /**
     * First we make the list to be sent
     */
    async deleteContacts() {
      console.log("Called!");
      const list = [];
      console.log("Length is " + this.selectedContacts.length);
      for (const i of this.selectedContacts) {
        const contact = this.contacts[i];
        list.push(contact.username);
      }
      const url = `${BACKEND_URL}deleteContacts`;
      console.log(list);
      await axios.post(url, list);
      this.selectedMails = [];

      await this.initContacts(`${BACKEND_URL}getContacts`);
    },
    /**
     * Take data from badran
     * */
    async filterMails() {
      const url = `${BACKEND_URL}filterContacts?typeOfSort=${
        this.valuesOfFilter[this.index]
      }&name=${this.filterName}`;
      console.log(url);
      await this.initContacts(url);
    },
    /**
     * Contacts data :
     *  String name;
     *  ArrayList<String> emails;
     * @returns {Promise<void>}
     */
    async initContacts(url) {
      this.contacts = [];
      let contacts;
      await axios.get(url).then(res => (contacts = res.data));
      console.log(contacts);
      if (contacts === null) return;
      for (let i = 0; i < contacts.length; i++) {
        const contact = {
          username: contacts[i].name,
          email: contacts[i].emails,
          id: i
        };
        console.log(contact);
        this.contacts.push(contact);
      }
    }
  },
  async mounted() {
    await this.initContacts(`${BACKEND_URL}getContacts`);
  }
};
</script>

<style scoped>
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

.search {
  padding: 15px;
  background: mediumpurple;
  box-shadow: 0 0 10px steelblue;
  position: relative;
  text-align: center;
}
.textl {
  font-size: 1.5rem;
}
.btn {
  flex-grow: 1;
  cursor: pointer;
  position: relative;
  padding: calc(var(--space) / 1.125) var(--space) var(--space);
  background: lightgray;
  font-size: 16px;
  width: 100px;
  height: 40px;
  border-radius: 8px;
}
.btn:hover,
.btn:focus {
  flex-grow: 2;
  color: white;
  outline: none;
  text-shadow: none;
  background: mediumpurple;
}
</style>
