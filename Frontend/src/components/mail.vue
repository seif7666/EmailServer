/* eslint-disable */
<template>
  <div class="A_mail">
    <div @click="selectMail">
      <h3 class="mail-Head">{{ head }}</h3>
      <span class="email"> {{ senderOrReceivers }}</span>
      <span class="priority">
        priority:{{ priority ? "Primary" : "Default" }}</span
      >
      <span class="mail-date">{{ date }}</span>
      <p class="mail-body">{{ body }}</p>
    </div>
    <div>
      <label ref="att">attachments:</label>
      <select
        id="sel"
        @change="switchView($event, $event.target.selectedIndex)"
        ref="select"
      >
      </select>
      <button id="dbtn" @click="download">download</button>
      <br />
      <label>
        Select
      </label>
      <input type="checkbox" v-model="marked" @click="checked" />
    </div>
  </div>
</template>

<script>
import { BACKEND_URL  } from "../main.js";

export default {
  props: [
    "id",
    "head",
    "email",
    "date",
    "body",
    "priority",
    "isSent",
    "attachments",
    "draft"
  ],
  name: "mail",
  data() {
    return {
      marked: false,
      senderOrReceivers: "",
      draft1: this.draft,
      index: 0
    };
  },
  methods: {
    selectMail() {
      if (this.draft1 === true) {
        console.log("Selected!");
        const wholeMail = {
          head: this.head,
          email: this.email,
          date: this.date,
          body: this.body,
          isSent: this.isSent,
          attachments: this.attachments,
          priority: this.priority
        };
        this.$emit("getTheMail", wholeMail);
        this.$router.push({
          name: "compose",
          params: {
            head: this.head,
            email: this.email,
            date: this.date,
            body: this.body,
            priority: this.priority,
            attachments: this.attachments,
            isSent: this.isSent
          }
        });
        return;
      }

      console.log("Selected!");

      this.$router.push({
        name: "singlemail",
        params: {
          id: this.id,
          head: this.head,
          email: this.email,
          date: this.date,
          body: this.body,
          attachments: this.attachments.attaches,
          isSent: this.isSent
        }
      });
    },
    checked() {
      // console.log("Marker is " + this.marked , this.id);
      this.$emit("getCheckedMail", this.id, this.marked);
    },
    download() {
      console.log(this.index);
      let typeOfMail;
      console.log(this.isSent);
      if (this.isSent === true) typeOfMail = "sent";
      else typeOfMail = "Inbox";

      const array = this.attachments.attaches[this.index].split("\\");
      //Here we download
      const url = `${BACKEND_URL}download?id=${
        this.id
      }&type=${typeOfMail}&name=${array[array.length - 1]}`;
      console.log(url);
      window.open(url);
    },
    switchView: function(event, selectedIndex) {
      const sel = document.querySelector("#sel");
      console.log(event, selectedIndex);
      sel.selectedIndex = selectedIndex;
      this.index = selectedIndex;
    }
  },
  mounted() {
    console.log("Is sent in mail.vue is " + this.isSent);
    if (this.isSent === true) {
      ///We show sender
      console.log("Entered");
      this.senderOrReceivers = "To " + this.email;
    } else {
      //We show Receiver
      this.senderOrReceivers = "From " + this.email;
    }

    const docSel = this.$refs.select;
    if (this.attachments === null || this.attachments.attaches === null) {
      docSel.remove();
      document.querySelector("#dbtn").remove();
      this.$refs.att.remove();
      return;
    }

    // console.log(docSel);
    console.log(this.attachments.attaches);
    for (let i = 0; i < this.attachments.attaches.length; i++) {
      const arr = this.attachments.attaches[i].split("\\");
      const option = new Option(arr[arr.length - 1], arr[arr.length - 1]);
      docSel.add(option, undefined);
    }
  }
};
</script>

<style scoped  >
.A_mail {
  padding: 15px;
  background: snow;
  box-shadow: 0 0 10px steelblue;
  position: relative;
  text-align: left;}
  .mail-date {
    display: block;
    padding: 0 0 5px;
    front-size: 14px;
  }
  .email {
    border-radius: 4px;
  }


#dbtn {
  flex-grow: 1;
  cursor: pointer;
  position: relative;
  padding: calc(var(--space) / 1.125) var(--space) var(--space);
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
</style>
