//우측 추천 문제들

<template>
    <div class="flex flex-col items-center mt-8 2xl:col-span-2">
        <div class="overflow-x-auto rounded-lg">
            <div class="inline-block min-w-full align-middle">
                <div class="overflow-hidden shadow sm:rounded-lg">
                    <GroupRating class="border border-white rounded-lg shadow md-4 sm:p-6 xl:p-8" />
                    <RecoPro class="m-10" />
                </div>
            </div>
        </div>
    </div>
</template>
  

<script>
import GroupRating from '@/components/mainpage/rightfixed/GroupRating.vue';
import RecoPro from '@/components/mainpage/rightfixed/RecoPro.vue';
export default {
    setup() {
    },

    components: { GroupRating, RecoPro }
};
</script>