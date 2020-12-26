/* eslint-disable */
<template>
  <div>
    <div class="multi-button">
      <button value="Sent" @click="sendOrDraft($event)">Send</button>
      <button value="Draft" @click="sendOrDraft($event)">Save To Draft</button>
      <button v-on:click="tomail()">Delete</button>
    </div>
    <br />
    <br />
    <div class="compose">
      <br />
      <label>Choose priority of the mail:</label>
      <input type="checkbox" @change="changePriority" />

      <div style="margin:10px">
        <label><strong>To: </strong></label>
      </div>
      <textarea
        class="email"
        v-model="email"
        placeholder="Enter the emails,In case of multiple mails,please insert ',' between them."
        cols="40"
        rows="2"
      ></textarea>

      <p>Title:</p>
      <input class="title" v-model="title" placeholder="Enter title" />
      <br />
      <textarea
        class="mail"
        v-model="body"
        placeholder="Enter the mail"
        cols="40"
        rows="5"
      ></textarea>
      <br />
      <file @getAttachments="setAttachments" />
    </div>
  </div>
</template>

<script>
import { BACKEND_URL, axios } from "../main";
import file from "../components/file";
import FileReader from "@/components/FileReader";
export default {
  components: { file },
  data() {
    return {
      name: "compose",
      email: this.$route.params.email,
      title: this.$route.params.head,
      body: this.$route.params.body,
      priority: this.$route.params.priority,
      // email: "",
      // title: "",
      // body: "",
      files: []
    };
  },
  methods: {
    tomail: function() {
      this.$router.push("/mailbox");
    },
    readFiles(files) {
      this.files = files;
      console.log(files);
    },
    changePriority() {
      this.priority = !this.priority;
    },
    /**
     * Here we gather the mail and send it
     * First:gather receivers
     */
    async sendOrDraft(e) {
      console.log(e.target.value);
      console.log(this.email);
      const url = `${BACKEND_URL}compose?type=${e.target.value}&title=${this.title}&body=${this.body}&receivers=${this.email}&priority=${this.priority}`;
      console.log(url);
      let formData = new FormData();
      for (let i = 0; i < this.files.length; i++)
        formData.append("myFile", this.files[i]);

      // list.push(receivers);
      // list.push(formData);
      if (e.target.value === "Draft") formData = null;

      let confirm = "";
      await axios.post(url, formData).then(res => (confirm = res.data + ""));
      if (confirm === "true") {
        alert("Done");
        this.$router.push("/mailbox");
      } else alert("Invalid mail");
    },
    setAttachments(files) {
      this.files = files;
      console.log(this.files);
    }
  },
  mounted() {
    if (this.priority === undefined) this.priority = false;
  }
};
</script>

<style scoped>
.compose {
  padding: 15px;
  background: snow;
  box-shadow: 0 0 10px steelblue;
  position: relative;
  text-align: left;
}
.title {
  display: block;

  padding: 0 0 5px;
  border-radius: 4px;

  front-size: 14px;
  padding: 14px 40px;
  width: 95%;
}
.email {
  front-size: 14px;

  border-radius: 4px;
  padding: 10px 40px;
  width: 95%;
}
.mail {
  border-radius: 5px;
  padding: 60px 40px;
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
</style>
