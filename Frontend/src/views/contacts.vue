<template>
  <div class="contacts">
    <div class="multi-button">
      <button v-on:click="toaddcontact()">Add Contact</button>
      <button>Delete</button>
      <button v-on:click="tomail()">back</button>
      <button v-on:click="tolog()">logout</button>
    </div>
    <br>
    <div>
      <contact
          v-for="contact in contacts"
          v-bind:key="contact.id"
          v-bind:id="contact.id"
          v-bind:nickname="contact.nickname"
          v-bind:username="contact.username"
          v-bind:email="contact.email"
          v-bind:phonenumber="contact.phonenumber"
          @getTheContact = "contactSelect"
          @getCheckedContact = "checkContact"
      />
      <p v-for="contact in contacts" v-bind:key="contact.id">
        {{contact.id}}
      </p>
    </div>
  </div>
</template>

<script>
import jsonContacts from "@/json/jsonContacts.json";
import contact from "@/components/contact";
export default {
  data() {
    return {
      contacts: jsonContacts,
      selectedContacts:[]
    }
  },
  name: "contacts",
  components: {contact},
  methods: {
    tomail: function () {
      this.$router.push("/mailbox");
    },
    tolog: function () {
        this.$router.push('/login')
    },
    toaddcontact: function() {
      this.$router.push("/addcontact");
    },
    contactSelect(contact){
      console.log(contact);
    },
    checkContact(id , marker){
      console.log("In check Contact : contacts.vue");
      console.log(typeof(marker))
      if(marker === false) {
        console.log("TRUE")
        this.selectedContacts.push(id)
      }
      else{
        const index = this.selectedContacts.indexOf(id);
        if (index > -1) {
          this.selectedContacts.splice(index, 1);
        }
      }
      console.table(this.selectedContacts)
    }
  }

}
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