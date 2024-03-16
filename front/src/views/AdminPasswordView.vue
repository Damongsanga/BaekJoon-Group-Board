<template>
  <div

    class="overflow-hidden flex items-center justify-center w-full h-full p-8 overflow-hidden from-black to-black bg-gradient-to-br p-8"

  >
    <div
      class="flex justify-center container mx-auto my-auto w-screen h-screen items-center flex-col"
    >
      <div class="text-slate-100 items-center flex m-3">

        <img
          src="@/assets/logoImg.png"
          class="h-20 p-1 invert"
          alt="Windster Logo"
        />
        <div class="text-center pb-3 text-3xl m-2 font-bold">
          {{ store.selectedGroup.groupName }} Admin Page

        </div>
      </div>

      <div

        class="flex flex-col items-center w-full pt-12 border border-white rounded-md md:w-3/4 lg:w-1/2"

      >
        <!-- ID input -->
        <div class="w-3/4 mb-6">
          <input
            v-model="password"
            type="password"
            name="password"

            class="text-mono w-full px-8 py-4 border-2 border-solid rounded bg-white placeholder:font-semibold hover:ring-1 hover:ring-gray-600 outline-slate-500 border-slate-300"

            placeholder="Enter Group Password"
          />
        </div>
        <!-- button -->
        <div class="w-3/4 mb-7">
          <button
            @click="store.adminLogin(password)"
            type="submit"
            id="password-btn"

            class="w-full py-4 font-bold bg-black border border-white rounded text-blue-50 hover:bg-slate-950"

          >
            Go To Admin Page
          </button>
        </div>
      </div>
      <div
        class="flex justify-center container mx-auto mt-6 text-slate-100 text-sm"
      >
        <div
          class="flex flex-col sm:flex-row justify-between md:w-1/2 items-center"
        ></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { adminStore } from "@/stores/adminInfo";
import { useRouter } from "vue-router";
import { useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();
const store = adminStore();

const password = ref("");
</script>

<style scoped></style>
