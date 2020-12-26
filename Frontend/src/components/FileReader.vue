/* eslint-disable */
<template>
  <div>
    <label class="text-reader">
      <label>
        Add attachments:
      </label>
      <br />
    </label>

    <div id="container">
      <form>
        <input
          type="text"
          id="name"
          class="attIn"
          placeholder="Enter path of attachment"
        />

        <input
          type="button"
          id="btnAdd"
          class="attbtn"
          value="add"
          @click="send"
        />
        <br />
        <label for="list"></label>
        <select ref="sel" id="list" name="list" multiple> </select>
        <br />

        <button size="2" class="attbtn" id="btnRemove" @click="send">
          Remove Selected attachment
        </button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: "FileReader",
  data() {
    return {
      files: []
    };
  },
  methods: {
    send() {
      console.table(this.files);

      this.$emit("load", this.files);
    }
  },
  mounted() {
    const btnAdd = document.querySelector("#btnAdd");
    const btnRemove = document.querySelector("#btnRemove");
    const sb = document.querySelector("#list");
    const name = document.querySelector("#name");

    btnAdd.onclick = e => {
      e.preventDefault();

      //validate the option
      if (this.title === "") {
        alert("Please enter the name.");
        return;
      }
      // create a new option
      const option = new Option(name.value, name.value);
      // add it to the list
      sb.add(option, undefined);
      this.files.push(name.value);

      // reset the value of the input
      name.value = "";
      name.focus();
    };

    // remove selected option
    btnRemove.onclick = e => {
      e.preventDefault();

      // save the selected option
      const selected = [];

      for (let i = 0; i < sb.options.length; i++) {
        selected[i] = sb.options[i].selected;
      }

      // remove all selected option
      let index = sb.options.length;
      while (index--) {
        console.log("Index at " + index + "is" + this.files[index]);
        if (selected[index]) {
          console.log("Removed");
          this.files.splice(index, 1);
          sb.remove(index);
        }

        // const ind = this.files.indexOf(selected[i]);
        // if (ind > -1) {
        //   this.files.splice(ind, 1);
      }
    };
  }
};
</script>

<style scoped>
.attIn {
  front-size: 14px;

  border-radius: 4px;
  padding: 10px 10px;
  width: 50%;
}
.attbtn {
  padding: 10px 30px;
  background: greenyellow;
  border-radius: 6px;
}

.attbtn:hover {
  background: deepskyblue;
}

.attbtn:active {
  background: blue;
}
</style>
