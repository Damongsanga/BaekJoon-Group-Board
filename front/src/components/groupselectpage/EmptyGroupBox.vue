<template>
  <NewGroupFormView v-if="isModalVisible" @close="reset()" />
  <div
    class="opacity-75 m-5 bg-white rounded-lg overflow-hidden transition-transform border border-white shadow-lg transformrounded-lg hover:scale-105 flex flex-col"
  >
    <div class="p-1 bg-slate-600"></div>

    <div class="p-8 pb-1">
      <h2 class="text-3xl font-bold text-slate-800 mb-4">New Group</h2>
    </div>
    <div class="p-4 mt-auto flex items-center justify-center">
      <svg
        class="mr-2 w-12 h-12 text-slate-800"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M12 2v20M2 12h20"
        ></path>
      </svg>
    </div>
    <div class="p-4 mt-auto">
      <button
        @click="moveToForm()"
        class="py-4 p-3 w-full bg-black text-white text-xl rounded px-4 py-2 hover:bg-slate-800 focus:outline-none focus:shadow-outline-blue active:bg-slate-400"
      >
        make new group
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { groupStore } from "@/stores/group";
import NewGroupFormView from "../../views/NewGroupFormView.vue";

const props = defineProps({
  isVisible: Boolean,
});
const store = groupStore();

const isModalVisible = ref(false);
const moveToForm = () => {
  isModalVisible.value = true;
};

const reset = () => {
  isModalVisible.value = false;
  store.fetchGroupList();
};
</script>

<style scoped></style>
