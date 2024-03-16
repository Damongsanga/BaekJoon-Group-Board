
<template>
  <div
    class="flex items-center justify-center w-full h-full p-8 overflow-hidden bg-black"
  >
    <div
      class="container flex flex-col items-center justify-center w-screen h-screen mx-auto my-auto"
    >
      <div class="flex items-center m-3 text-slate-100">
        <img
          src="@/assets/logoImg.png"
          class="h-20 p-1 invert"
          alt="Windster Logo"
        />
        <div class="p-3 font-serif text-4xl text-center">BBOARD</div>
      </div>

      <div
        class="flex flex-col items-center w-full pt-12 border border-white rounded-md md:w-3/4 lg:w-1/2"
      >
        <div class="w-3/4 mb-6">
          <input
            v-model="solvedACID"
            type="text"
            name="text"
            id="solvedACID"
            class="text-mono w-full px-8 py-4 border-2 border-solid rounded bg-white placeholder:font-semibold hover:ring-1 hover:ring-gray-600 outline-slate-500 border-slate-300"
            placeholder="solved.ac ID"
          />
        </div>
        <!-- button -->
        <div class="w-3/4 mb-7">
          <button
            @click="store.login(solvedACID)"
            type="submit"
            id="login-btn"
            class="w-full py-4 font-bold bg-black border border-white rounded text-blue-50 hover:bg-slate-950"
          >

            LOGIN
          </button>
        </div>
        <div class="w-3/4 mb-12">

          <button
            @click="store.regist(solvedACID)"
            type="submit"
            id="regist-btn"
            class="w-full py-4 font-bold bg-black border border-white rounded text-blue-50 hover:bg-slate-950"
          >

            REGIST
          </button>
        </div>
      </div>

      <div
        class="container flex justify-center mx-auto mt-6 text-sm text-slate-100"
      >
        <div
          class="flex flex-col items-center justify-between sm:flex-row md:w-1/2"
        ></div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { loginStore } from "@/stores/loginInfo";

const store = loginStore();
const solvedACID = ref(null);
</script>

<style scoped></style>
