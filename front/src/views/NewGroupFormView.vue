<template>
  <!-- 오버레이 -->

  <div
    class="fixed inset-0 z-40 flex items-center justify-center bg-black bg-opacity-50"
  >
    <!-- 모달 컨테이너 -->
    <div
      class="z-50 w-full max-w-lg p-10 text-white bg-black border border-white rounded-lg shadow-2xl"
    >
      <div
        class="w-full max-w-lg px-10 py-8 mx-auto border rounded-lg shadow-2xl modal"
      >

        <div class="max-w-md mx-auto space-y-3">
          <h3 class="text-lg font-semibold">&#128540; Make New Group!!</h3>
          <div>
            <label class="block py-1">Group Name</label>

            <input
              v-model="groupName"
              type="text"
              class="text-black w-full px-2 py-2 font-mono border rounded shadow hover:border-indigo-600 ring-1 ring-inset ring-gray-300"
            />

            <p class="hidden px-2 mt-2 text-sm text-gray-600">Text helper</p>
          </div>
          <div>
            <label class="block py-1">Password</label>

            <input
              v-model="password"
              type="password"
              class="text-black w-full px-2 py-2 font-mono border rounded shadow hover:border-indigo-600 ring-1 ring-inset ring-gray-300"
            />
          </div>
          <div class="flex items-center gap-3 pt-3">
            <button
              @click="addgroup()"
              class="px-4 py-2 border rounded-lg shadow hover:border-indigo-600 ring-1 ring-inset ring-indigo-700"
            >
              Make New Group
            </button>
            <button
              @click="goBackToGroupPage()"
              class="px-4 py-2 border rounded-lg shadow hover:border-indigo-600 ring-1 ring-inset ring-gray-300"
            >

              Go Back
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";


const emit = defineEmits(["close"]);


const groupName = ref("");
const password = ref("");

const addgroup = async () => {
  try {
    const jsonData = {
      groupName: groupName.value,
      password: password.value,
    };

    const response = await axios({
      url: `http://localhost:8080/api/user-group/group/add`,
      method: "POST",
      withCredentials: true,
      data: jsonData,
    });

    goBackToGroupPage();

    if (!response.data) {
      alert("오류가 생겼습니다. 담비한테 물어보세용!");
    }

  } catch (err) {
    console.error(err.message);
  }
};

const goBackToGroupPage = () => {
  emit('close');
};
</script>

<style scoped></style>
