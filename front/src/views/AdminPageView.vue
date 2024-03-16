<template>
  <div class="w-full h-full p-8 bg-black">
    <div class="max-w-md p-3 mx-auto bg-black border border-white rounded-xl">
      <div class="text-center">
        <h1 class="m-2 text-xl font-bold text-slate-100">
          {{ store.selectedGroup.groupName }} Admin Page
        </h1>
        <h3 class="m-2 text-4xl font-bold text-white">UserList</h3>
      </div>
      <div class="mt-3">
        <ul>
          <UserList
            v-for="user in store.allUserList"
            :user="user"
            :groupUserList="store.groupUserList"
            :key="user.userId"
          />
        </ul>
      </div>
      <div class="mt-8">
        <button
          @click="store.deleteGroup()"
          class="p-2 text-red-500 border-2 border-red-500 hover:text-white hover:bg-red-500"
        >
          Remove Group
        </button>
        <button
          @click="returnToGroupPage()"
          class="p-2 ml-4 text-indigo-500 border-2 border-indigo-500 hover:text-white hover:bg-indigo-500"
        >
          Go Back
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from "vue";
import { adminStore } from "@/stores/adminInfo";
import { useRouter } from "vue-router";
import UserList from "../components/adminpage/UserList.vue";

const store = adminStore();
const router = useRouter();

const returnToGroupPage = () => {
  router.replace({ name: "group" });
};

onMounted(() => {
  store.reset();
});
</script>

<style scoped></style>
