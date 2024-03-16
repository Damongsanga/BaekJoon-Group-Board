<template>
  <div class="flex flex-col items-center mt-8">
    <div v-for="level in 14" :key="level" class="flex justify-center mb-2 space-x-2">
      <div v-for="box in level" :key="level + '-' + box">
        <a :href="getBoxLink(level, box)" target="_blank">
          <div :class="['border-2', getBoxClass(level, box), getBoxSize(level)]" @mouseover="handleMouseOver(level, box)"
            @mouseleave="handleMouseLeave"></div>
        </a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { mainApiStore } from "@/stores/main-api";
import { selectedUserStore } from "@/stores/userInfo";
import { changeboxStore } from "@/stores/changebox-db";
const mainApiStoreInst = mainApiStore();
const userInfoInst = selectedUserStore();
const changeboxStoreInst = changeboxStore();

const handleMouseOver = (level, box) => {
  let idx = (level * (level - 1)) / 2 + box - 1;
  if (level > 5) {
    idx -= 5;
  }
  changeboxStoreInst.overProblemNum = mainApiStoreInst.getTop100ProNum[idx];
};

const handleMouseLeave = () => {
  if (changeboxStoreInst.overProblemNum.value) {
    changeboxStoreInst.overProblemNum.value = 0;
  }
};

const getBoxClass = (level, box) => {
  let idx = (level * (level - 1)) / 2 + box - 1;
  if (level > 5) {
    idx -= 5;
  }
  const problemNum = mainApiStoreInst.getTop100ProNum[idx];
  const getUserId = userInfoInst.userId;

  if (typeof mainApiStoreInst.setUserTop100[getUserId] === "undefined")
    return "hover:bg-gray-300 border-gray-300";

  const isSolved =
    getUserId && mainApiStoreInst.setUserTop100[getUserId].includes(problemNum);
  return isSolved
    ? "hover:bg-orange-500 border-orange-500"
    : "hover:bg-gray-300 border-gray-300";
};

const getBoxSize = (level) => {
  if (level < 5) {
    return "w-5 h-5 rounded-sm sm:rounded-md md:w-6 md:h-6"; // 20px
  } else if (level === 5) {
    return "hidden w-5 h-5 rounded-sm sm:rounded-md md:w-6 md:h-6";
  } else {
    return "w-4 h-4 rounded-sm sm:rounded-md sm:w-4 sm:h-4 md:w-5 md:h-5"; // 16px
  }
};

const getBoxLink = (level, box) => {
  let idx = (level * (level - 1)) / 2 + box - 1;
  if (level > 5) {
    idx -= 5;
  }
  const problemNum = mainApiStoreInst.getTop100ProNum[idx];
  return `https://www.acmicpc.net/problem/${problemNum}`;
};
</script>
