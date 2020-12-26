/* eslint-disable */
<template>
  <div>
    <label class="file">
      <input
        class="attbtn"
        type="file"
        id="file"
        multiple
        @change="handleUpload($event, 1)"
        aria-label="File browser example"
      />
      <span class="file-custom"></span>
    </label>

    <div id="container">
      <form>
        <br />
        <select id="list" name="list" multiple> </select>
        <br />

        <button
          size="2"
          class="attbtn"
          id="btnRemove"
          @click="handleUpload($event, 0)"
        >
          Remove Selected attachment
        </button>
      </form>
    </div>
  </div>
</template>

<script>
const MEGA_BYTE = 50;
const MAX_FILE_SIZE = MEGA_BYTE * 1024 * 1024; //50MB

export default {
  name: "file",
  data() {
    return {
      paths: [],
      files: []
    };
  },
  methods: {
    handleUpload(event, flag) {
      if (flag === 1) this.handleFileChooser(event);
      else this.handleRemove(event);

      console.log(this.paths);
      console.log(this.files);

      this.$emit("getAttachments", this.files);

      // const url = `${BACKEND_URL}file`;
      // axios.post(url , formData, {
      //     headers:{
      //         "Content-Type": "multipart/form-data"
      //     }
      // } );
    },
    handleFileChooser(event) {
      const sb = document.querySelector("#list");
      const files = event.target.files;
      // const formData = new FormData();
      // formData.append('myFile', files[i]);
      for (let i = 0; i < files.length; i++) {
        if (files[i].size > MAX_FILE_SIZE) {
          alert(
            "File : " +
              files[i].name +
              " won't be added as it's larger than " +
              MEGA_BYTE +
              "MB"
          );
          continue;
        }
        console.log(files[i].name);
        const option = new Option(files[i].name, files[i].name);
        sb.add(option, undefined);
        this.paths.push(files[i].name);
        this.files.push(files[i]);
      }
    },
    handleRemove(event) {
      event.preventDefault();
      const sb = document.querySelector("#list");

      // save the selected option
      const selected = [];

      for (let i = 0; i < sb.options.length; i++) {
        selected[i] = sb.options[i].selected;
      }

      // remove all selected option
      let index = sb.options.length;
      while (index--) {
        if (selected[index]) {
          this.paths.splice(index, 1);
          this.files.splice(index, 1);
          sb.remove(index);
        }
      }
    }
  }
};
</script>

<style scoped>
*,
*:before,
*:after {
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
}

body {
  margin: 0;
  padding: 2rem 1.5rem;
  font: 1rem/1.5 "PT Sans", Arial, sans-serif;
  color: #5a5a5a;
}
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
