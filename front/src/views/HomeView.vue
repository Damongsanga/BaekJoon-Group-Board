<script setup></script>

<template>
  <main class="w-screen h-screen bg-black"></main>
</template>

<script setup>
import { onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
onMounted(() => {
  router.replace({ name: "login" });
});
</script>
