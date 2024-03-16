<template>
    <div class="flex items-center">
        <div class="items-center hidden lg:flex">
            <span class="mr-5 text-base font-normal text-gray-500"> {{ loginUser.handle }} ❤️</span>
            <button
                class="middle none center rounded-lg bg-orange-500 py-3 px-6 font-sans text-xs font-bold uppercase text-white shadow-md shadow-orange-500/20 transition-all hover:shadow-lg hover:shadow-orange-500/40 focus:opacity-[0.85] focus:shadow-none active:opacity-[0.85] active:shadow-none disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"
                data-ripple-light="true" @click="logout()">
                Logout
            </button>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const loginUser = JSON.parse(localStorage.getItem('user'));

const router = useRouter();
const logout = function logoutBnt() {
    localStorage.clear();
    router.replace({ name: "login" });
}

</script>