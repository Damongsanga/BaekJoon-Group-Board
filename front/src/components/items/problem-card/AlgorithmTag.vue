<template>
    <span :class="tagClass">
        <svg xmlns="http://www.w3.org/2000/svg" class="fill-current" viewBox="0 0 16 16" width="7" height="7">
            <path
                d="M6.368 1.01a.75.75 0 0 1 .623.859L6.57 4.5h3.98l.46-2.868a.75.75 0 0 1 1.48.237L12.07 4.5h2.18a.75.75 0 0 1 0 1.5h-2.42l-.64 4h2.56a.75.75 0 0 1 0 1.5h-2.8l-.46 2.869a.75.75 0 0 1-1.48-.237l.42-2.632H5.45l-.46 2.869a.75.75 0 0 1-1.48-.237l.42-2.632H1.75a.75.75 0 0 1 0-1.5h2.42l.64-4H2.25a.75.75 0 0 1 0-1.5h2.8l.46-2.868a.75.75 0 0 1 .858-.622ZM9.67 10l.64-4H6.33l-.64 4Z">
            </path>
        </svg>
        <span :class="textClass">{{ algorithm }}</span>
    </span>
</template>
  
<script setup>
import { computed } from 'vue';

const props = defineProps({
    algorithm: String
});

const textClass = computed(() => {
    return props.algorithm.length > 8 ? 'text-xs' : 'text-sm';
});

const tagClass = computed(() => {
    return 'inline-flex items-center px-3 m-1 ml-4 uppercase rounded-full leading-sm' +
        (props.algorithm.length > 8 ? ' text-xs' : ' text-sm');
});
</script>
  