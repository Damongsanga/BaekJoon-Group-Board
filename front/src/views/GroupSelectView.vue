<template>
  <div v-if="store.isLoading">로딩중</div>
  <div v-else>

    <!-- from-blue-900 via-indigo-800 to-indigo-500 bg-gradient-to-br -->
    <div class="h-screen bg-black">
      <div class="flex pt-5 pl-5">
        <p class="flex items-center p-5 border-2 border-white rounded w-fit">
          <img :src="store.loginUser.profileImageUrl" class="w-10 h-10 rounded-lg" />
        <p class="pl-2 font-bold text-center text-white">{{ store.loginUser.handle }}</p>
        </p>
      </div>
      <div class="flex items-center justify-center py-12">
        <div v-if="!store.groupList.length"
          class="flex grid items-center justify-center grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
          <EmptyGroupBox />
        </div>
        <div v-else class="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
          <GroupBox v-for="group in store.groupList" :key="group.id" :groupInfo="group"
            :userList="store.groupUserMap[group.id]" />
          <!-- 그룹길이가 2개이하라면 -->
          <EmptyGroupBox v-if="store.groupList.length <= 2" />
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { groupStore } from "@/stores/group";
import GroupBox from "@/components/groupselectpage/GroupBox.vue";
import EmptyGroupBox from "@/components/groupselectpage/EmptyGroupBox.vue";

const store = groupStore();

onMounted(() => {
  store.fetchGroupList();
});
</script>

<style scoped></style>
