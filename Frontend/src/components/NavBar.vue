<template>
  <nav class="navbar">
    <div class="nav-container">
      <div class="nav-brand">
      </div>
      <ul class="nav-menu" :class="{ 'active': isMenuOpen }">
        <li class="nav-item">
          <router-link to="/" class="nav-link">首页</router-link>
        </li>
        <li class="nav-item">
          <router-link to="/login" class="nav-link">登录</router-link>
        </li>
      </ul>
      <div class="nav-toggle" :class="{ 'active': isMenuOpen }" @click="toggleMenu">
        <span></span>
        <span></span>
        <span></span>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'NavBar',
  data() {
    return {
      isMenuOpen: false
    }
  },
  methods: {
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen
    }
  }
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background: transparent;
  z-index: 1000;
}

.nav-container {
  max-width: 100%;
  margin: 0 auto;
  padding: 0 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 50px;
}

.nav-brand .brand-link {
  font-size: 24px;
  font-weight: 700;
  color: #637ceca2;
  text-decoration: none;
}

.nav-menu {
  display: flex;
  list-style: none;
  gap: 40px;
  margin: 0;
  padding: 0;
}

.nav-item {
  display: flex;
  align-items: center;
}

.nav-link {
  text-decoration: none;
  color: #ffffff;
  font-weight: 500;
  font-size: 16px;
  transition: color 0.3s ease;
  position: relative;
}

.nav-link:hover {
  color: #667eea;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 0;
  height: 2px;
  background: #667eea;
  transition: width 0.3s ease;
}

.nav-link:hover::after {
  width: 100%;
}

.nav-toggle {
  display: none;
  flex-direction: column;
  cursor: pointer;
  padding: 8px;
}

.nav-toggle span {
  width: 25px;
  height: 3px;
  background: #ffffff;
  margin: 4px 0;
  transition: all 0.3s ease;
  border-radius: 2px;
}

@media (max-width: 768px) {
  .nav-menu {
    position: absolute;
    top: 50px;
    left: 0;
    width: 100%;
    background: rgba(46, 56, 84, 0.98);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    flex-direction: column;
    align-items: center;
    padding: 20px 0;
    gap: 20px;
    display: none;
    border-bottom-left-radius: 12px;
    border-bottom-right-radius: 12px;
  }
  
  .nav-menu.active {
    display: flex;
  }
  
  .nav-menu .nav-link {
    color: #ffffff;
    font-size: 18px;
    padding: 10px 20px;
    border-radius: 8px;
    transition: all 0.3s ease;
  }
  
  .nav-menu .nav-link:hover {
    color: #667eea;
    background: rgba(255, 255, 255, 0.1);
  }
  
  .nav-toggle {
    display: flex;
  }
  
  .nav-toggle.active span:nth-child(1) {
    transform: rotate(-45deg) translate(-5px, 6px);
  }
  
  .nav-toggle.active span:nth-child(2) {
    opacity: 0;
  }
  
  .nav-toggle.active span:nth-child(3) {
    transform: rotate(45deg) translate(-5px, -6px);
  }
}
</style>
