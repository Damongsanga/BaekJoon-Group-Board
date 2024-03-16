<template>
    <div class="flex flex-row justify-between w-full ">
        <RatingProfile v-for="ranker in users" :key="ranker.userId" :ranker="ranker" />
    </div>
</template>

<script>
import RatingProfile from '@/components/mainpage/rightfixed/ratingprofile/RatingProfile.vue';
import { fixedBoxStore } from '@/stores/fixedbox-db';

export default {
    components: {
        RatingProfile
    },
    setup() {
        const fixedBoxInst = fixedBoxStore();
        const users = fixedBoxInst.rankTop3.slice(0, 3);
        return { users };
    }
}
</script>
