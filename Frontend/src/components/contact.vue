/* eslint-disable */
<template>
  <div class="A_contact">
    <div @click="selectContact">
      <h3 class="username">Name of contact: {{ username }}</h3>
      <span class="email"> Emails: {{ email }}</span>
      <br />
    </div>
    <div>
      <br />
      <label>
        Select
      </label>
      <input @change="checked" type="checkbox" />
    </div>
  </div>
</template>

<script>
export default {
  props: ["id", "username", "email"],
  name: "contact",
  data() {
    return {
      marked: false
    };
  },
  methods: {
    selectContact() {
      console.log("Selected!");

      let string = "";
      for (let i = 0; i < this.email.length; i++) {
        string += this.email[i];
        if (i !== this.email.length - 1) string += ",";
      }
      console.log(string);
      // const wholeContact={
      //   username : this.username,
      //   email : string,
      // }
      //
      // this.$emit('getTheContact' , wholeContact);
      this.$router.push({
        name: "singlecontact",
        params: {
          username: this.username,
          email: string,
          id: this.id
        }
      });
    },
    checked() {
      this.marked = !this.marked;
      console.log("Marker is " + this.marked, this.id);
      this.$emit("getCheckedContact", this.id, this.marked);
    }
  }
};
</script>
<style scoped>
.A_contact {
  padding: 15px;
  background: snow;
  box-shadow: 0 0 10px steelblue;
  position: relative;
  text-align: left;
}
.nickname {
  display: block;
  padding: 0 0 5px;
  front-size: 14px;
}
.email {
  border-radius: 4px;
}</style
>le>
