<template>
  <div
    class="flex flex-col m-5 overflow-hidden transition-transform transform bg-white rounded-lg shadow-lg hover:scale-105">
    <div class="p-1 bg-slate-600"></div>
    <div class="p-8">
      <h2 class="mb-4 text-3xl font-bold text-slate-800">
        {{ p.groupInfo.groupName }}
      </h2>
      <ul class="grid grid-cols-3 gap-2 mb-6 text-sm text-gray-600">
        <li class="flex items-center mb-2 font-bold text-l text-slate-950">
          <svg class="w-4 h-4 mr-2 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
          </svg>
          {{ userCount }} Users
        </li>
        <li class="flex items-center mb-2" v-for="user in p.userList" :key="user.userId">
          <img :src="user.profileImageUrl" class="w-5 h-5 rounded-lg" />
          <div class="pl-2 text-sm">{{ user.handle }}</div>
        </li>
      </ul>
    </div>
    <div class="flex flex-col justify-end p-4 mt-auto">
      <div class="flex justify-items-end">
        <button @click="moveToAdminLogin()"
          class="px-4 py-2 m-3 ml-0 text-white bg-orange-500 rounded hover:bg-orange-400 focus:outline-none focus:shadow-outline-blue active:bg-orange-800">
          admin
        </button>
        <button @click="leaveGroup()"
          class="px-4 py-2 m-3 ml-0 text-white bg-red-500 rounded hover:bg-red-400 focus:outline-none focus:shadow-outline-blue active:bg-red-800">
          leave
        </button>
      </div>
      <button @click="enterMainPage()"
        class="w-full p-3 px-4 py-2 py-4 text-xl text-white bg-black rounded hover:bg-slate-800 focus:outline-none focus:shadow-outline-blue active:bg-slate-400">

        Enter
      </button>
    </div>
  </div>
</template>

<script setup>
import { storeToRefs } from "pinia";
import { defineProps, computed } from "vue";
import { useRouter } from "vue-router";
import { groupStore } from "@/stores/group";

const store = groupStore();

const router = useRouter();
const p = defineProps({
  groupInfo: Object,
  userList: Object,
});

const userCount = computed(() => {
  if (p.userList != undefined) return p.userList.length;
  return 0;
});

const moveToAdminLogin = () => {
  store.selectedGroup = p.groupInfo;
  router.replace({
    name: "adminPassword",
  });
};

const leaveGroup = () => {
  store.leaveGroup(p.groupInfo.id);
  router.replace({ name: "group" });
};

// const groupId = computed(() => {
//   if (!p.groupInfo) return 0;
//   return;
// });

const enterMainPage = () => {

  store.selectedGroup = p.groupInfo;
  localStorage.setItem('group', JSON.stringify(p.groupInfo));
  router.replace({
    name: "about",
  });
};
</script>

<style scoped></style>
