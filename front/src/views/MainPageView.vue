<template>
  <div v-if="mainApiStoreInst.isLoading">로딩중</div>
  <!-- This is an example component -->
  <div v-else class="h-screen overflow-hidden bg-black">
    <NavigationHeader />
    <div class="flex pt-16 overflow-hidden bg-black">
      <div id="main-content" class="relative w-full h-full overflow-y-hidden">
        <main class="h-full">
          <div class="h-full px-20 pt-10">


            <div class="grid w-full h-full grid-cols-1 gap-4 2xl:grid-cols-7">
              <LeftChangeBox />
              <RightFixedBox />
            </div>
          </div>
        </main>
        <p class="fixed bottom-0 left-0 w-full my-10 text-sm text-center text-gray-500">
          &copy; 2023
          <a href="#" class="hover:underline" target="_blank">DuckDam</a>.
          All rights reserved.
        </p>

      </div>
    </div>
  </div>
</template>

<script>
import { onMounted } from "vue";




import { useRoute } from "vue-router";
import NavigationHeader from "@/components/mainpage/header/NavigationHeader.vue";
import LeftChangeBox from "@/components/mainpage/LeftChangeBox.vue";
import RightFixedBox from "@/components/mainpage/RightFixedBox.vue";
import { mainApiStore } from "@/stores/main-api";

export default {
  setup() {
    const mainApiStoreInst = mainApiStore();

    onMounted(() => {
      mainApiStoreInst.fetchData();
    });

    return { mainApiStoreInst };
  },

  components: { NavigationHeader, LeftChangeBox, RightFixedBox },
};
</script>

<style></style>
