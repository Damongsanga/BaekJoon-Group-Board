<template>
  <li class="p-1 rounded-lg">
    <div class="flex flex-row justify-between align-middle">
      <div class="p-1">
        <p class="font-mono text-white">
          {{ p.user.handle }}
        </p>
      </div>

      <button
        v-if="p.groupUserList.some((u) => u.userId === p.user.userId)"
        @click="store.removeUser(user.userId)"
        class="flex p-2 text-red-500 border-2 border-red-500 rounded-lg hover:text-white hover:bg-red-500"
      >
        <svg
          class="w-6 h-6"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <circle cx="12" cy="12" r="10" />
          <line x1="15" y1="9" x2="9" y2="15" />
          <line x1="9" y1="9" x2="15" y2="15" />
        </svg>
        <span> Remove</span>
      </button>
      <button
        v-else
        @click="store.addUser(p.user.userId)"
        class="flex p-2 ml-2 text-green-500 border-2 border-green-500 rounded-lg hover:text-white hover:bg-green-500"
      >
        <svg
          class="w-6 h-6"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          stroke-width="2"
          stroke="currentColor"
          fill="none"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <path stroke="none" d="M0 0h24v24H0z" />
          <circle cx="12" cy="12" r="9" />
          <line x1="9" y1="12" x2="15" y2="12" />
          <line x1="12" y1="9" x2="12" y2="15" />
        </svg>
        <span>Add</span>
      </button>
    </div>
    <hr class="mt-2" />
  </li>
</template>

<script setup>
import { adminStore } from "@/stores/adminInfo";
const store = adminStore();

const p = defineProps({
  user: {
    type: Object,
    required: true,
  },
  groupUserList: {
    type: Object,
  },
});
</script>

<style scoped></style>
