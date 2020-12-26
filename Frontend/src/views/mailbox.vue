<template>
  <div class="mailbox">
    <div class="userB">
      <span class="user"> {{ username }}</span>
      <button class="btnu" v-on:click="tocontacts()">contacts</button>
      <button class="btnu" v-on:click="tolog('A')">logout</button>
    </div>
    <br />
    <div class="multi-button">
      <button value="Inbox" @click="changeMessageType($event)">Inbox</button>
      <button value="Sent" @click="changeMessageType($event)">Sent</button>
      <button value="Trash" @click="changeMessageType($event)">Trash</button>
      <button value="Draft" @click="changeMessageType($event)">Draft</button>
    </div>

    <br />
    <br />
    <div class="multi-button">
      <button v-on:click="tocompose()">compose</button>
      <button @click="moveToTrash">{{ TrashButton }}</button>
    </div>
    <br />
    <div class="search">
      <br />
      <label class="textl">Choose type of sorting </label>
      <select id="sorting" @change="onChangeSort($event)">
        <option value="Date">Date</option>
        <option value="Priority">Priority</option>
        <option value="Sender">Sender</option>
        <option value="Subject">Subject</option>
        <option value="Body">Body</option>
      </select>
      <br />
      <button class="btn" @click="sortMails">Sort</button>

      <br />
      <br />
      <label class="textl">Search for </label>
      <input v-model="filterName" />
      <label class="textl"> in </label>

      <select id="filter" @change="onChangeFilter($event)">
        <option value="Receivers">Receivers</option>
        <option value="Subject">Subject</option>
        <option value="Body">Body</option>
        <option value="Date">Date</option>
        <option value="Primary">Primary</option>
        <option value="Default">Default</option>
      </select>

      <br />
      <label class="textl"> Date : </label>
      <input type="date" v-model="filter_date" />
      <label class="textl"> priority : </label>
      <select id="priority">
        <option value="very important">none</option>
        <option value="very important">very important</option>
        <option value="important">important</option>
        <option value="normal">normal</option>
        <option value="not important">not important</option>
      </select>

      <br />
      <br />
      <button class="btn" @click="filterMails">search</button>
    </div>

    <br />
    <div>
      <mail
        v-for="mail in mails"
        v-bind:key="mail.id"
        v-bind:id="mail.id"
        v-bind:head="mail.title"
        v-bind:email="mail.email"
        v-bind:date="mail.date"
        v-bind:body="mail.body"
        v-bind:isSent="mail.isSent"
        v-bind:priority="mail.priority"
        v-bind:attachments="mail.attachments"
        v-bind:draft="draft"
        @getTheMail="mailSelect"
        @getCheckedMail="checkMail"
      />
      <p v-for="mail in Mails" v-bind:key="mail.id">
        {{ mail.id }}
      </p>
    </div>
  </div>
</template>
<script>
import mail from "../components/mail.vue";
import { BACKEND_URL, axios } from "../main.js";

export default {
  data: function() {
    return {
      mails: [],
      selectedMails: [], //List of selected mails by user.

      TrashButton: "Delete",

      filterType: "",
      filterName: "",
      typeOfMails: "Sent",

      sortType: "",
      draft: "",

      username: ""
    };
  },
  name: "mailbox",
  components: { mail },
  methods: {
    onChangeFilter(event) {
      this.filterType = event.target.value;
    },
    onChangeSort(event) {
      this.sortType = event.target.value;
    },
    mailSelect(mail) {
      console.log(mail);
    },
    tocompose: function() {
      this.$router.push("/compose");
    },
    tocontacts: function() {
      this.$router.push("/contacts");
    },
    tolog: function(message) {
      if (message == "A") {
        this.$router.push("/login");
      }
    },
    checkMail(id, marker) {
      alert(this.draft);
      console.log("In check mail : mailbox.vue");
      console.log(typeof marker);
      if (marker === false) {
        console.log("TRUE");
        this.selectedMails.push(id);
      } else {
        const index = this.selectedMails.indexOf(id);
        if (index > -1) {
          this.selectedMails.splice(index, 1);
        }
      }
      console.table(this.selectedMails);
    },
    async changeMessageType(event) {
      this.typeOfMails = event.target.value;
      console.log(this.typeOfMails);
      this.TrashButton = "Delete";

      if (this.typeOfMails === "Sent") {
        const url = `${BACKEND_URL}getMails?type=${this.typeOfMails}`;
        await this.getSentMails(url);
      } else if (this.typeOfMails === "Inbox") {
        const url = `${BACKEND_URL}getMails?type=${this.typeOfMails}`;
        await this.getSentMails(url, false);
      } else if (this.typeOfMails === "Draft") {
        this.draft = true;
        const url = `${BACKEND_URL}getMails?type=${this.typeOfMails}`;
        await this.getSentMails(url, true);
      } else if (this.typeOfMails === "Trash") {
        const url = `${BACKEND_URL}getMails?type=${this.typeOfMails}`;
        await this.getSentMails(url);
        this.TrashButton = "Restore";
      }
    },
    /**
     * Here we will send Deleted messages to Trash
     * */
    async moveToTrash() {
      let url;
      if (this.typeOfMails === "Trash") {
        //Here we will restore
        url = `${BACKEND_URL}moveToTrash?type=${this.typeOfMails}&toTrash=false`;
        await axios.post(url, this.selectedMails);
      } else {
        url = `${BACKEND_URL}moveToTrash?type=${this.typeOfMails}&toTrash=true`;
        await axios.post(url, this.selectedMails);
      }
      this.selectedMails = [];
      url = `${BACKEND_URL}getMails?type=${this.typeOfMails}`;
      await this.getSentMails(url);
    },
    async sortMails() {
      console.log(this.sortType);
      console.log(this.typeOfMails);
      let url = "";
      if (this.sortType === "Priority")
        url = `${BACKEND_URL}sortPrior?inboxOrSent=${this.typeOfMails}`;
      else
        url = `${BACKEND_URL}sort?folder=${this.typeOfMails}&type=${this.sortType}`;
      console.log(url);
      await this.getSentMails(url);
    },
    /**
     * We need filter name.
     * filterType
     * Inbox or sent
     * @returns {Promise<void>}
     */
    async filterMails() {
      console.log(this.filterType);
      console.log(this.filterName);
      console.log(this.typeOfMails);
      let url = "";
      if (this.filterType === "Default" || this.filterType === "Primary")
        url = `${BACKEND_URL}filter?filterName=${this.filterType}&subOrRec=Priority&inboxOrSent=${this.typeOfMails}`;
      else
        url = `${BACKEND_URL}filter?filterName=${this.filterName}&subOrRec=${this.filterType}&inboxOrSent=${this.typeOfMails}`;

      console.log(url);
      await this.getSentMails(url);
    },
    async getSentMails(url, sent = true) {
      this.mails = []; //Clear list Of Mails
      console.log("Sent is " + sent);
      let sentMails;
      await axios.get(url).then(res => (sentMails = res.data));
      console.table(sentMails);
      for (let i = 0; i < sentMails.length; i++) {
        console.log(sentMails[i]);
        const mail = {
          id: sentMails[i].id,
          priority: sentMails[i].priority,
          title: sentMails[i].header.subject,
          email: sentMails[i].header.reciever,
          body: sentMails[i].body.body,
          date: sentMails[i].time,
          attachments: sentMails[i].attaches,
          isSent: sent
        };
        if (!sent) mail.email = sentMails[i].header.sender;
        this.mails.push(mail);
      }
      console.table(this.mails);
    }
  },

  async mounted() {
    this.filterType = "Receivers";
    this.sortType = "Date";

    let url = `${BACKEND_URL}getUserUsername?`;
    await axios.get(url).then(res => (this.username = res.data));
    url = `${BACKEND_URL}getMails?type=Sent`;
    await this.getSentMails(url);
  }
};
</script>

<style scoped >
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

.user {
  color: #d04764;
  font-family: "Lobster", cursive;
  font-size: 36px;
  font-weight: normal;
  line-height: 48px;
  margin: 0 0 18px;
  text-shadow: 1px 0 0 #fff;
}
.btnu {
  background-color: lightgray;
  width: 100px;
  height: 40px;
}

.userB {
  padding: 15px;
  background: royalblue;
  box-shadow: 0 0 10px steelblue;
  position: relative;
  text-align: center;
}
.btnu:hover,
.btnu:focus {
  background: mediumpurple;
}
</style>
