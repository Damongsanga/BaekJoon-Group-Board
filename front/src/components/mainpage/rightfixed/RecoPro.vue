<template>
    <div v-for="item in selectedProData" :key="item.id">
        <span v-if="item.algorithm !== undefined">
            <ProblemCard :proData="item" />
        </span>
    </div>
</template>
  

<script>
import { computed } from 'vue';
import ProblemCard from '@/components/items/problem-card/ProblemCard.vue';
import { fixedBoxStore } from '@/stores/fixedbox-db';

export default {
    components: {
        ProblemCard
    },
    setup() {
        const fixedBoxInst = fixedBoxStore();
        // recomProAlgoObject 객체에서 무작위로 세 개의 값을 선택
        const selectedProData = computed(() => {
            const allKeys = Object.keys(fixedBoxInst.recomProAlgoObject);
            const selectedKeys = getRandomKeys(allKeys, 3);

            return selectedKeys.map(key => fixedBoxInst.recomProAlgoObject[key]);
        });
        console.log(selectedProData)
        return { selectedProData };
    }
}

function getRandomKeys(keys, num) {
    const shuffled = [...keys].sort(() => 0.5 - Math.random());
    return shuffled.slice(0, num);
}
</script>
