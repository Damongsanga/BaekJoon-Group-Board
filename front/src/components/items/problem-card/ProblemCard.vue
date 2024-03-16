<template>
    <a :href="`https://www.acmicpc.net/problem/${proData.problemId}`" target="_blank">
        <div class="w-64 m-8 text-white border border-white shadow-lg bg-opacity-60 rounded-xl">
            <div class="flex items-center">
                <div class="w-10 h-10 pl-3 mt-2 ">
                    <img class="object-cover"
                        :src="`https://static.solved.ac/tier_small/${proData.level}.svg` || 'https://static.solved.ac/misc/360x360/default_profile.png'">
                </div>
                <h2 class="mt-2 mb-2 ml-5 mr-3 font-bold">{{ proData.titleKo }}</h2>
            </div>
            <div class="flex m-4 justify-stretch w-50 ">
                <div class="w-50">
                    <AlgorithmTag v-for="(algorithm, index) in proData.algorithm" :key="index"
                        :class="shuffledColorPairs[index % shuffledColorPairs.length].join(' ')" :algorithm="algorithm" />
                </div>
            </div>
        </div>
    </a>
</template>


<script>
import AlgorithmTag from '@/components/items/problem-card/AlgorithmTag.vue';

export default {
    components: { AlgorithmTag },
    props: {
        proData: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            // 초기 색상 배열 정의
            colorPairs: [
                ["border border-blue-300", "text-blue-800"],
                ["border border-green-300", "text-green-800"],
                ["border border-red-300", "text-red-800"],
                // ... 기타 색상 쌍
            ],
        };
    },
    computed: {
        shuffledColorPairs() {
            return this.shuffleArray([...this.colorPairs]);
        }
    },
    methods: {
        shuffleArray(array) {
            let currentIndex = array.length, temporaryValue, randomIndex;

            // 남은 요소가 없을 때까지 반복
            while (0 !== currentIndex) {
                // 남은 요소 중에서 하나를 임의로 선택
                randomIndex = Math.floor(Math.random() * currentIndex);
                currentIndex -= 1;

                // 선택한 요소와 현재 요소를 교환
                temporaryValue = array[currentIndex];
                array[currentIndex] = array[randomIndex];
                array[randomIndex] = temporaryValue;
            }

            return array;
        }
    }



}
</script>

