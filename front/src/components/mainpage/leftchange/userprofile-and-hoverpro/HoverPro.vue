<template>
    <div v-if="visible" class="flex justify-centerw-80 h-90">
        <ProblemCard v-if="objCheck" :pro-data="changeboxStoreInst.getProblemObj" />
    </div>
</template>

<script setup>
import { computed } from 'vue';
import ProblemCard from '@/components/items/problem-card/ProblemCard.vue';
import { changeboxStore } from '@/stores/changebox-db';

const changeboxStoreInst = changeboxStore();
const objCheck = computed(() =>
    changeboxStoreInst.getProblemObj);
const visible = computed(() => {
    if (!changeboxStoreInst.getProblemObj.level) return false;
    return true;
}

)
</script>
