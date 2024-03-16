<template>
    <div :style="{ position: 'absolute', top: `${mouseY}px`, left: `${mouseX}px` }"
        class="bg-black bg-opacity-50 border-opacity-50 rounded-md ">
        <div class="p-1 mt-px text-xs font-semibold text-white border border-yellow-800 rounded-md">
            {{ props.userName }}
        </div>
    </div>
</template>



<script setup>

const props = defineProps({
    userName: String
});

</script>
  
<style>
.mini-profile-container {
    position: fixed;
    top: 0;
    left: 0;
}
</style>
  