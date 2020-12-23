<template>
  <div class="A_contact">
    <div   @click = "selectContact">
      <h3 class = "nickname">{{ nickname }}</h3>
      <h3 class="username">{{ username }}</h3>
      <span class = "email"> {{ email }}</span>
      <br>
      <span class ="phonenumber">{{phoneNumber}}</span>
    </div>
    <div>
      <br>
      <label>
        Select
      </label>
      <input type="checkbox">
    </div>
  </div>
</template>

<script>
export default {
  props: ["id","nickname", "username", "email", "phonenumber"],
  name: "contact",
  data() {
    return {
      marked: false
    };
  },
    methods:{
    selectContact(){
      console.log("Selected!");
      const wholeContact={
        nickname : this.nickname,
        username : this.username,
        email : this.email,
        phonenumber : this.phonenumber,
      }
      this.$emit('getTheContact' , wholeContact);
      this.$router.push({name: "singlecontact", params: {
          nickname:this.nickname,
          username:this.username,
          email:this.email,
          phonenumber:this.phonenumber}});
    },
      checked(){
        // console.log("Marker is " + this.marked , this.id);
        this.$emit('getCheckedContact' , this.id , this.marked);
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
.nickname{
  display: block;
  padding: 0 0 5px;
  front-size:14px;
}
.email{
  border-radius: 4px;
}
</style>