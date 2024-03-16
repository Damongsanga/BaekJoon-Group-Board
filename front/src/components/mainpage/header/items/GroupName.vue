<template>
    <span class="text-xl text-yellow-300">
        {{ group.groupName }}
    </span>
</template>

<script setup>
import { mainApiStore } from '@/stores/main-api';
import { computed } from 'vue';
const mainApiStoreInst = mainApiStore();
const group = JSON.parse(localStorage.getItem('group'));
const groupInfo = computed(() =>
    mainApiStoreInst.selectedGroup
);


</script>