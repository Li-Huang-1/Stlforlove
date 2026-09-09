<template>
  <div class="homepage-container">
    <div class="top-container">
      <div class="waves-container">
        <div class="wave wave1"></div>
        <div class="wave wave2"></div>
      </div>
    </div>
    <div class="bottom-container">
      <div class="cards-wrapper">
        <div class="cards-flex-row">
          <div class="card card-tall profile-card">
            <div class="profile-header">
              <div class="profile-bg-wrapper">
                <div class="profile-bg"></div>
              </div>
              <div class="avatar-container">
                <img src="@/assets/image.jpg" alt="Avatar" class="avatar">
              </div>
            </div>
            <div class="profile-content">
              <div class="username">Li_Huang1</div>
              <div class="location">China</div>
              <div class="intro-text">Hi! I'm 黎凰</div>
              <div class="logos-container">
                <div class="logo-item">
                  <router-link to="/articles/tech" title="技术栈">
                    <img src="@/assets/网络品牌树.png" alt="技术栈">
                  </router-link>
                </div>
                <div class="logo-item">
                  <router-link to="/articles/log" title="日志">
                    <img src="@/assets/文件-2.png" alt="日志">
                  </router-link>
                </div>
                <div class="logo-item">
                  <router-link to="/articles/project" title="项目">
                    <img src="@/assets/项目-1.png" alt="项目">
                  </router-link>
                </div>
              </div>
              <div class="social-links-container">
                <a href="https://space.bilibili.com/410739371" target="_blank" rel="noopener noreferrer" class="social-link">
                  <img src="@/assets/bilibili.svg" alt="bilibili">
                </a>
                <a href="https://github.com/Li-Huang-1" target="_blank" rel="noopener noreferrer" class="social-link">
                  <img src="@/assets/github.svg" alt="github">
                </a>
                <a href="#" @click.prevent class="social-link social-link-qq">
                  <img src="@/assets/QQ.svg" alt="QQ">
                  <span class="qq-tooltip"></span>
                </a>
                <a href="https://steamcommunity.com/profiles/76561198810593378/" target="_blank" rel="noopener noreferrer" class="social-link">
                  <img src="@/assets/steam.svg" alt="steam">
                </a>
              </div>
              <div class="stats-container">
                <div class="stat-item">
                  <div class="stat-value">{{ articleCount }}</div>
                  <div class="stat-label">文章</div>
                 </div>
                <div class="stat-item">
                  <div class="stat-value">{{ visitorCount }}</div>
                  <div class="stat-label">访客</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{{ runningDays }}</div>
                  <div class="stat-label">天数</div>
                </div>
              </div>
            </div>
          </div>
          <div class="card-column-wrapper">
            <div class="card card-header">
              <div class="card-content">
                <span class="header-text">人道洛阳花似锦，偏我来时不逢春</span>
              </div>
            </div>
            <div class="card card-large player-card">
              <audio ref="audioPlayer" :src="currentSong.audioSrc" @timeupdate="onTimeUpdate" @loadedmetadata="onLoadedMetadata" @ended="onAudioEnded"></audio>
              <div class="player-content">
                <div class="album-cover">
                  <div class="cover-wrapper" :class="{ 'playing': isPlaying }">
                    <img 
                      :src="currentSong.cover" 
                      :alt="currentSong.title" 
                      class="cover-image"
                      @error="handleCoverError"
                    />
                    <div v-if="!currentSong.cover" class="cover-placeholder">
                      <span class="placeholder-icon">♪</span>
                    </div>
                  </div>
                </div>
                <div class="player-info">
                  <div class="song-title">{{ currentSong.title }}</div>
                  <div class="song-artist">{{ currentSong.artist }}</div>
                  <div class="progress-section">
                    <span class="time current-time">{{ formatTime(currentTime) }}</span>
                    <div class="progress-bar-wrapper">
                      <div class="progress-bar" ref="progressBar">
                        <div class="progress-fill" :style="{ width: progressPercent + '%' }"></div>
                        <div class="progress-thumb" :style="{ left: progressPercent + '%' }"></div>
                      </div>
                    </div>
                    <span class="time total-time">{{ formatTime(duration) }}</span>
                  </div>
                  <div class="controls">
                    <div class="controls-left">
                      <button class="control-btn prev-btn" @click="prevSong" title="上一曲 (Ctrl+←)">
                        <span class="btn-icon">◀◀</span>
                      </button>
                      <button class="control-btn play-btn" @click="togglePlay" title="播放/暂停 (空格)">
                        <span class="btn-icon">{{ isPlaying ? '❚❚' : '▶' }}</span>
                      </button>
                      <button class="control-btn next-btn" @click="nextSong" title="下一曲 (Ctrl+→)">
                        <span class="btn-icon">▶▶</span>
                      </button>
                    </div>
                    <button class="control-btn loop-btn" :class="{ 'active': isLooping }" @click="toggleLoop" title="单曲循环">
                      <span class="btn-icon">↻</span>
                    </button>
                  </div>
                </div>
              </div>
            </div>
            <div class="cards-row-wrapper">
              <button class="card card-rectangle-top" @click="goToArticleList()">文  章</button>
              <button class="card card-rectangle-top" @click="goToMessageWall()">留 言 板</button>
              <button class="card card-rectangle-top" @click="showComingSoon()">杂  项</button>
            </div>
            <div class="cards-row-wrapper bottom-cards">
              <div class="card card-square">
                <div class="card-content">
                  <h3>功能开发中</h3>
                  <p>敬请期待</p>
                </div>
              </div>
              <div class="card card-rectangle">
                <div class="card-content">
                  <h3>功能开发中</h3>
                  <p>敬请期待</p>
                </div>
              </div>
            </div>
          </div>
          <div class="card-stack">
            <div class="card card-clock">
              <div class="card-content clock-content">
                <div class="clock-date">
                  <span class="date-year">{{ year }}</span>
                  <span class="date-separator">年</span>
                  <span class="date-month">{{ month }}</span>
                  <span class="date-separator">月</span>
                  <span class="date-day">{{ day }}</span>
                  <span class="date-separator">日</span>
                </div>
                <div class="clock-time">
                  <span class="time-digit hours">{{ hours }}</span>
                  <span class="time-separator">:</span>
                  <span class="time-digit minutes">{{ minutes }}</span>
                </div>
                <div class="clock-weekday">{{ weekday }}</div>
              </div>
            </div>
            <div class="card card-gallery">
              <div class="gallery-container">
                <div class="gallery-image-wrapper">
                  <img 
                    src="@/assets/Lcrad.jpg" 
                    alt="Gallery" 
                    class="gallery-image"
                    @error="handleGalleryError"
                  />
                  <div class="gallery-overlay">
                    <div class="gallery-title">平凡であるより、むしろ死を選ぶ</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="footer-card" :class="{ 'show': showFooterCard }">
      <div class="footer-card-content">
        <div class="footer-card-title">ICP备案信息</div>
        <div class="footer-card-records">
          <div class="footer-card-info">
            <span class="record-label">备案号：</span>
            <span class="record-value">闽ICP备2026002991号-1</span>
          </div>
          <span class="record-divider">|</span>
          <div class="footer-card-info">
            <span class="record-label">公安备案：</span>
            <span class="record-value">闽公网安备35058102000458号</span>
          </div>
        </div>
        <div class="footer-card-copyright">Copyright © by Li_Huang1 All Rights Reserved.</div>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'HomePage',
  data() {
    return {
      articleCount: 0,
      visitorCount: 1,
      showFooterCard: false,
      songs: [
        {
          id: 1,
          title: 'Evening',
          artist: 'Corn Wave',
          cover: require('@/assets/SongCover.jpg'),
          audioSrc: require('@/assets/Evening.mp3')
        },
        {
          id: 2,
          title: 'To the Moon',
          artist: 'Kan "Reives" Gao',
          cover: require('@/assets/Tothemoon.jpg'),
          audioSrc: require('@/assets/Tothemoon.mp3')
        },
      ],
      currentSongIndex: 0,
      isPlaying: false,
      currentTime: 0,
      duration: 245,
      progressInterval: null,
      isDragging: false,
      isLooping: false,
      currentDate: '',
      currentSystemTime: '',
      timerInterval: null,
      year: '',
      month: '',
      day: '',
      weekday: '',
      hours: '',
      minutes: '',
      seconds: ''
    }
  },
  computed: {
    runningDays() {
      const startDate = new Date('2026-07-27')
      const today = new Date()
      const diffTime = today - startDate
      const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))
      return diffDays
    },
    currentSong() {
      return this.songs[this.currentSongIndex]
    },
    progressPercent() {
      return (this.currentTime / this.duration) * 100
    }
  },
  methods: {
    formatTime(seconds) {
      const mins = Math.floor(seconds / 60)
      const secs = Math.floor(seconds % 60)
      return `${mins}:${secs.toString().padStart(2, '0')}`
    },
    togglePlay() {
      const audio = this.$refs.audioPlayer
      if (!audio) return
      
      if (this.isPlaying) {
        audio.pause()
      } else {
        audio.play().catch(err => {
          console.error('播放失败:', err)
        })
      }
      this.isPlaying = !this.isPlaying
    },
    prevSong() {
      const audio = this.$refs.audioPlayer
      this.currentSongIndex = (this.currentSongIndex - 1 + this.songs.length) % this.songs.length
      this.currentTime = 0
      this.isPlaying = false
      if (audio) {
        audio.pause()
        audio.currentTime = 0
      }
    },
    nextSong() {
      const audio = this.$refs.audioPlayer
      this.currentSongIndex = (this.currentSongIndex + 1) % this.songs.length
      this.currentTime = 0
      this.isPlaying = false
      if (audio) {
        audio.pause()
        audio.currentTime = 0
      }
    },
    onTimeUpdate() {
      const audio = this.$refs.audioPlayer
      if (audio) {
        this.currentTime = audio.currentTime
      }
    },
    onLoadedMetadata() {
      const audio = this.$refs.audioPlayer
      if (audio) {
        this.duration = audio.duration
      }
    },
    onAudioEnded() {
      if (this.isLooping) {
        const audio = this.$refs.audioPlayer
        if (audio) {
          audio.currentTime = 0
          audio.play().catch(err => {
            console.error('循环播放失败:', err)
          })
        }
        this.currentTime = 0
      } else {
        this.isPlaying = false
        this.nextSong()
      }
    },
    toggleLoop() {
      this.isLooping = !this.isLooping
    },
    handleCoverError(e) {
      e.target.style.display = 'none'
    },
    handleGalleryError(e) {
      e.target.parentElement.innerHTML = '<div class="gallery-placeholder"><span class="placeholder-text">图片加载失败</span></div>'
    },
    setupProgressBar() {
      const progressBar = this.$refs.progressBar
      if (progressBar) {
        progressBar.addEventListener('click', this.handleProgressClick)
        progressBar.addEventListener('mousedown', this.handleDragStart)
        document.addEventListener('mousemove', this.handleDrag)
        document.addEventListener('mouseup', this.handleDragEnd)
      }
    },
    stopProgress() {
      const progressBar = this.$refs.progressBar
      if (progressBar) {
        progressBar.removeEventListener('click', this.handleProgressClick)
        progressBar.removeEventListener('mousedown', this.handleDragStart)
        document.removeEventListener('mousemove', this.handleDrag)
        document.removeEventListener('mouseup', this.handleDragEnd)
      }
    },
    handleProgressClick(e) {
      if (this.isDragging) return
      const progressBar = e.currentTarget
      const rect = progressBar.getBoundingClientRect()
      const x = e.clientX - rect.left
      const percent = (x / rect.width) * 100
      this.setProgress(percent)
    },
    handleDragStart() {
      this.isDragging = true
    },
    handleDrag(e) {
      if (!this.isDragging) return
      const progressBar = this.$refs.progressBar
      if (!progressBar) return
      const rect = progressBar.getBoundingClientRect()
      let x = e.clientX - rect.left
      x = Math.max(0, Math.min(x, rect.width))
      const percent = (x / rect.width) * 100
      this.setProgress(percent)
    },
    handleDragEnd() {
      if (this.isDragging) {
        this.isDragging = false
      }
    },
    setProgress(percent) {
      const audio = this.$refs.audioPlayer
      this.currentTime = (percent / 100) * this.duration
      if (audio) {
        audio.currentTime = this.currentTime
      }
    },
    handleKeydown(e) {
      if (e.code === 'Space' && !e.target.closest('input, textarea')) {
        e.preventDefault()
        this.togglePlay()
      } else if (e.ctrlKey && e.code === 'ArrowLeft') {
        e.preventDefault()
        this.prevSong()
      } else if (e.ctrlKey && e.code === 'ArrowRight') {
        e.preventDefault()
        this.nextSong()
      }
    },
    updateTime() {
      const now = new Date()
      const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
      
      // 更新日期字段
      this.year = String(now.getFullYear())
      this.month = String(now.getMonth() + 1).padStart(2, '0')
      this.day = String(now.getDate()).padStart(2, '0')
      this.weekday = weekDays[now.getDay()]
      
      // 更新时间字段
      this.hours = String(now.getHours()).padStart(2, '0')
      this.minutes = String(now.getMinutes()).padStart(2, '0')
      this.seconds = String(now.getSeconds()).padStart(2, '0')
      
      // 保留原有字段用于兼容
      this.currentDate = `${this.year}年${this.month}月${this.day}日 ${this.weekday}`
      this.currentSystemTime = `${this.hours}:${this.minutes}:${this.seconds}`
    },
    startTimer() {
      this.updateTime()
      this.timerInterval = setInterval(() => {
        this.updateTime()
      }, 1000)
    },
    stopTimer() {
      if (this.timerInterval) {
        clearInterval(this.timerInterval)
        this.timerInterval = null
      }
    },
    handleScroll() {
      const scrollTop = window.scrollY || document.documentElement.scrollTop
      const windowHeight = window.innerHeight
      const documentHeight = document.documentElement.scrollHeight
      if (scrollTop + windowHeight >= documentHeight - 10) {
        this.showFooterCard = true
      } else {
        this.showFooterCard = false
      }
    },
    goToArticleList() {
      this.$router.push('/articles')
    },
    goToMessageWall() {
      this.$router.push('/message-wall')
    },
    showComingSoon() {
      this.$message({
        message: '功能开发中，敬请期待',
        type: 'info',
        duration: 2000
      })
    },
    async loadArticleCount() {
      try {
        const data = await request({
          url: '/public/article/count',
          method: 'get'
        })
        this.articleCount = data || 0
      } catch (error) {
        console.error('获取文章总数失败:', error)
        this.articleCount = 0
      }
    },
    async loadVisitorCount() {
      try {
        // 检查当前浏览器会话是否已经计数
        const hasCounted = sessionStorage.getItem('visitor_counted')
        
        if (hasCounted) {
          // 已经计数过，只获取当前计数
          const data = await request({
            url: '/public/visitor/count',
            method: 'get'
          })
          this.visitorCount = data?.count || 1
        } else {
          // 首次访问，增加计数并标记
          const data = await request({
            url: '/public/visitor/increment',
            method: 'get'
          })
          this.visitorCount = data?.count || 1
          // 标记已计数
          sessionStorage.setItem('visitor_counted', 'true')
        }
      } catch (error) {
        console.error('获取访客数失败:', error)
        this.visitorCount = 1
      }
    },
  },
  // 挂载
  mounted() {
    this.$nextTick(() => {
      this.setupProgressBar()
    })
    window.addEventListener('keydown', this.handleKeydown)
    window.addEventListener('scroll', this.handleScroll)
    this.startTimer()
    this.loadArticleCount()
    this.loadVisitorCount()
  },
  beforeUnmount() {
    this.stopProgress()
    this.stopTimer()
    window.removeEventListener('keydown', this.handleKeydown)
    window.removeEventListener('scroll', this.handleScroll)
  }
}
</script>

<style scoped>
.homepage-container {
    width: 100%;
    max-width: 100vw;
    overflow-x: hidden;
}

.top-container {
    width: 100%;
    height: 40.4167vw;
    background-image: url('@/assets/top-card.png');
    background-size: cover;
    background-position: 50% 100%;
    background-repeat: no-repeat;
    position: relative;
    overflow: hidden;
}

.top-container::before {
    content: 'StLforLove';
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-family: 'Noto Serif SC', 'Georgia', 'Times New Roman', serif;
    font-size: 8vw;
    font-weight: 300;
    color: rgba(255, 255, 255, 0.04);
    pointer-events: none;
    z-index: 3;
    white-space: nowrap;
}

.top-container::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 500px;
    background: linear-gradient(to bottom, 
        transparent 0%, 
        rgba(77, 84, 110, 0.02) 20%,
        rgba(77, 84, 110, 0.05) 35%,
        rgba(77, 84, 110, 0.1) 50%,
        rgba(77, 84, 110, 0.2) 65%,
        rgba(77, 84, 110, 0.35) 80%,
        rgba(77, 84, 110, 0.5) 100%
    );
    z-index: 5;
}

.bottom-container {
    width: 100%;
    min-height: 875px;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: flex-start;
    padding: 40px 20px;
    overflow: hidden;
}

.bottom-container::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: url('@/assets/bottom-card1.jpg');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    filter: blur(10px);
    -webkit-filter: blur(10px);
    transform: scale(1.1);
    z-index: 0;
}

.bottom-container::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 500px;
    background: linear-gradient(to bottom, 
        #2E3854 0%, 
        rgba(46, 56, 84, 0.95) 8%,
        rgba(46, 56, 84, 0.88) 15%,
        rgba(55, 63, 92, 0.75) 22%,
        rgba(63, 71, 100, 0.6) 30%,
        rgba(70, 78, 108, 0.48) 38%,
        rgba(77, 84, 110, 0.38) 46%,
        rgba(77, 84, 110, 0.28) 55%,
        rgba(77, 84, 110, 0.18) 64%,
        rgba(77, 84, 110, 0.1) 73%,
        rgba(77, 84, 110, 0.05) 83%,
        rgba(77, 84, 110, 0.02) 92%,
        transparent 100%
    );
    z-index: 0;
}

.bottom-container > * {
    position: relative;
    z-index: 1;
}

.cards-wrapper {
    width: 100%;
    max-width: 1240px;
    margin: 0 auto;
    display: flex;
    flex-direction: column;
    gap: 24px;
}

.cards-flex-row {
    display: flex;
    gap: 24px;
    justify-content: center;
    align-items: flex-start;
    transform: scale(0.95);
}

.card-column-wrapper {
    display: flex;
    flex-direction: column;
    gap: 16px;
    width: 100%;
    max-width: 640px;
}

.cards-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 24px;
    justify-items: center;
}

.card {
    background-color: rgba(255, 255, 255, 0.55);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    border: 1px solid rgba(224, 224, 224, 0.5);
    border-radius: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    overflow: hidden;
    transition: transform 0.3s ease, box-shadow 0.3s ease, border-color 0.3s ease;
}

.card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
    border-color: rgba(208, 208, 208, 0.7);
}

.card-content {
    padding: 12px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    text-align: center;
}

.player-card {
    background: linear-gradient(135deg, rgba(65, 102, 140, 0.5) 0%, rgba(167,188,202,0.5) 100%);
    border-color: rgba(65, 102, 140, 0.5);
}

.player-card .card-content {
    display: none;
}

.player-content {
    padding: 24px;
    display: flex;
    gap: 24px;
    align-items: center;
    height: 100%;
}

.album-cover {
    flex-shrink: 0;
}

.cover-wrapper {
    width: 120px;
    height: 120px;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.cover-wrapper.playing {
    animation: rotate 8s linear infinite;
}

.cover-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.cover-placeholder {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background: linear-gradient(135deg, #4a5568 0%, #2d3748 100%);
}

.placeholder-icon {
    font-size: 3rem;
    color: rgba(255, 255, 255, 0.3);
}

.player-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 12px;
    min-width: 0;
}

.song-title {
    font-family: 'Noto Serif SC', 'Roboto', 'Georgia', serif;
    font-size: 1.5rem;
    font-weight: 600;
    color: #ffffff;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.song-artist {
    font-family: 'Noto Serif SC', 'Roboto', 'Georgia', serif;
    font-size: 1rem;
    color: rgba(255, 255, 255, 0.7);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.progress-section {
    display: flex;
    align-items: center;
    gap: 12px;
}

.time {
    font-family: 'Roboto', 'Helvetica Neue', Arial, sans-serif;
    font-size: 0.875rem;
    color: rgba(255, 255, 255, 0.6);
    min-width: 40px;
}

.progress-bar-wrapper {
    flex: 1;
    height: 6px;
    cursor: pointer;
}

.progress-bar {
    width: 100%;
    height: 100%;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 3px;
    position: relative;
    cursor: pointer;
    overflow: visible;
}

.progress-fill {
    height: 100%;
    background: linear-gradient(90deg, #3b82f6 0%, #06b6d4 100%);
    border-radius: 3px;
    transition: width 0.1s linear;
}

.progress-thumb {
    position: absolute;
    top: 50%;
    width: 14px;
    height: 14px;
    background: #ffffff;
    border-radius: 50%;
    transform: translate(-50%, -50%);
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
    cursor: grab;
    transition: left 0.1s linear;
    opacity: 0;
}

.progress-bar:hover .progress-thumb {
    opacity: 1;
}

.progress-bar:active .progress-thumb {
    cursor: grabbing;
    transform: translate(-50%, -50%) scale(1.2);
}

.controls {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 8px;
}

.controls-left {
    display: flex;
    align-items: center;
    gap: 16px;
}

.control-btn {
    background: rgba(255, 255, 255, 0.1);
    border: none;
    border-radius: 50%;
    padding: 12px;
    cursor: pointer;
    transition: all 0.2s ease;
    display: flex;
    justify-content: center;
    align-items: center;
}

.control-btn:hover {
    background: rgba(255, 255, 255, 0.2);
    transform: scale(1.05);
}

.control-btn:active {
    transform: scale(0.95);
}

.prev-btn, .next-btn{
    width: 44px;
    height: 44px;
}

.loop-btn {
  margin-right: 16px;
  width: 25px;
  height: 25px;
}

.loop-btn.active {
    background: linear-gradient(135deg, #1d3d80 0%, #385fb2 100%);
    box-shadow: 0 4px 12px rgba(99, 102, 241, 0.4);
    color: #ffffff;
}

.play-btn {
    width: 56px;
    height: 56px;
    background: linear-gradient(135deg, #3966a2 0%, #3257a7 100%);
    box-shadow: 0 4px 12px rgba(99, 102, 241, 0.4);
}

.play-btn:hover {
    background: linear-gradient(135deg, rgb(17, 74, 245) 0%, #2f65da 100%);
    box-shadow: 0 6px 16px rgba(99, 102, 241, 0.5);
    transform: scale(1.08);
}

.btn-icon {
    font-size: 1rem;
    color: #ffffff;
    font-weight: 300;
}

.play-btn .btn-icon {
    font-size: 1.25rem;
    padding-left: 2px;
}

@keyframes rotate {
    from {
        transform: rotate(0deg);
    }
    to {
        transform: rotate(360deg);
    }
}

.card h3 {
    font-size: 1.5rem;
    color: #333;
    margin-bottom: 12px;
    font-weight: 600;
}

.card p {
    font-size: 1rem;
    color: #666;
    margin-bottom: 16px;
}

.card-size {
    display: inline-block;
    padding: 6px 12px;
    background-color: #f0f0f0;
    border-radius: 6px;
    font-size: 0.875rem;
    color: #888;
}

.card-large {
    width: 100%;
    max-width: 640px;
    height: 250px;
}

.card-header {
    width: 100%;
    max-width: 640px;
    height: 50px;
    background: linear-gradient(to bottom, rgba(46,58,87,0.5) 0%, rgba(131,146,164,0.1) 100%);
    border-color: rgba(65, 102, 140, 0.5);
}

.cards-row-wrapper {
    display: flex;
    gap: 16px;
    width: 100%;
    max-width: 640px;
}

.card-rectangle-top {
    flex: 1;
    height: 50px;
    background: linear-gradient(90deg, rgba(164,186,201,0.4) 0%, rgba(129,162,186,0.4) 50%, rgba(76,111,143,0.4) 100%);
    border-color: rgba(38, 58, 78, 0.5);
    border: 1px solid rgba(38, 58, 78, 0.33);
    padding: 0;
    font-family: 'Noto Serif SC', 'Roboto', 'Georgia', serif;
    font-size: 0.95rem;
    font-weight: 500;
    color: rgba(255, 255, 255, 0.85);
    cursor: pointer;
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
    white-space: nowrap;
    transition: all 0.3s ease;
  }

  .card-rectangle-top:hover {
    background: linear-gradient(90deg, rgba(164,186,201,0.55) 0%, rgba(129,162,186,0.55) 50%, rgba(76,111,143,0.55) 100%);
    color: #ffffff;
  }

  .card-rectangle-top:active {
    background: linear-gradient(90deg, rgba(164,186,201,0.55) 0%, rgba(129,162,186,0.55) 50%, rgba(76,111,143,0.55) 100%);
  }

.card-square {
    flex: 0 0 auto;
    width: 218px;
    height: 218px;
    background: linear-gradient(135deg, rgba(65, 102, 140, 0.5) 0%, rgba(167,188,202,0.5) 100%);
    border-color: rgba(65, 102, 140, 0.5);
}

.card-rectangle {
    flex: 1;
    height: 218px;
    background: linear-gradient(135deg, rgba(90, 105, 130, 0.5) 0%, rgba(144, 164, 181, 0.5) 100%);
    border-color: rgba(65, 102, 140, 0.5);
}

.bottom-cards .card-rectangle {
    height: 218px;
}

.bottom-cards .card-content {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
    padding: 12px;
    gap: 12px;
}

.quick-entry-btn {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 10px 20px;
    background: linear-gradient(135deg, #4a90c2 0%, #2563eb 100%);
    border: none;
    border-radius: 20px;
    cursor: pointer;
    transition: all 0.3s ease;
    box-shadow: 0 2px 8px rgba(69, 105, 144, 0.4);
}

.quick-entry-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(69, 105, 144, 0.6);
}

.quick-entry-btn:active {
    transform: translateY(0);
}

.quick-entry-btn .btn-icon {
    font-size: 1.1rem;
}

.quick-entry-btn .btn-text {
    font-family: 'Noto Serif SC', 'Roboto', 'Georgia', serif;
    font-size: 0.95rem;
    font-weight: 500;
    color: #ffffff;
}

.card-rectangle-top h3,
.card-rectangle-top h4,
.card-rectangle-top p,
.bottom-cards h3,
.bottom-cards p { 
    color: #ffffffac;
}

.card-header .card-content {
    padding: 0 24px;
    justify-content: center;
}

.header-text {
    font-family: 'Noto Serif SC', 'Roboto', 'Georgia', serif;
    line-height: 50px;
    font-size: 0.975rem;
    font-weight: 500;
    color: #B7C9D1;
    font-style: italic;
}

.time-display {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 2px;
}

.date-text {
    font-family: 'Roboto', 'Helvetica Neue', sans-serif;
    font-size: 0.75rem;
    color: rgba(183, 201, 209, 0.8);
}

.time-text {
    font-family: 'Roboto Mono', 'Courier New', monospace;
    font-size: 0.875rem;
    font-weight: 600;
    color: #B7C9D1;
    letter-spacing: 1px;
}

.clock-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    padding: 24px 20px;
    gap: 24px;
}

.clock-time {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 16px;
}

.time-digit {
    font-family: 'Roboto Mono', 'Courier New', monospace;
    font-size: 3rem;
    font-weight: 900;
    color: #ffffff;
    text-shadow: 
        0 0 4px rgba(255, 255, 255, 0.8),
        0 0 8px rgba(37, 70, 142, 0.9),
        0 0 16px rgba(37, 70, 142, 0.6),
        1px 1px 2px rgba(0, 0, 0, 0.4);
    min-width: 60px;
    text-align: center;
    letter-spacing: 2px;
    transition: transform 0.3s ease, text-shadow 0.3s ease;
}

.time-separator {
    font-family: 'Roboto Mono', 'Courier New', monospace;
    font-size: 2.4rem;
    font-weight: 900;
    color: rgba(255, 255, 255, 0.9);
    text-shadow: 0 0 10px rgba(37, 70, 142, 0.8);
    animation: separatorPulse 1.5s ease-in-out infinite;
}

@keyframes separatorPulse {
    0%, 100% { 
        opacity: 0.5; 
        transform: scale(0.95);
    }
    50% { 
        opacity: 1; 
        transform: scale(1.05);
    }
}

.clock-date {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-wrap: wrap;
    gap: 8px;
    font-family: 'Noto Serif SC', 'Georgia', serif;
    padding: 8px 16px;
    /* background: rgba(255, 255, 255, 0.05);
    border-radius: 20px;
    backdrop-filter: blur(5px); */
}

.date-year, .date-month, .date-day {
    font-size: 1.1rem;
    font-weight: 700;
    color: #ffffff;
    text-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
    transition: color 0.3s ease;
}

.date-separator {
    font-size: 0.95rem;
    color: rgba(183, 201, 209, 0.6);
    margin: 0 2px;
}

.clock-weekday {
    font-family: 'Noto Serif SC', 'Georgia', serif;
    font-size: 1rem;
    font-weight: 600;
    color: #ffffff;
    padding: 6px 18px;
    background: rgba(61, 96, 172, 0.2);
    border: 1px solid rgba(37, 70, 142, 0.3);
    border-radius: 20px;
    text-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
    /* animation: weekdayGlow 3s ease-in-out infinite; */
}

@keyframes weekdayGlow {
    0%, 100% { 
        box-shadow: 0 0 5px rgba(37, 70, 142, 0.3);
    }
    50% { 
        box-shadow: 0 0 15px rgba(37, 70, 142, 0.6);
    }
}

.card-tall {
    width: 100%;
    max-width: 240px;
    height: 520px;
}

.profile-card {
    display: flex;
    flex-direction: column;
    height: 616px;
    font-family: 'Noto Serif SC', 'Roboto', 'Georgia', serif;
    background: linear-gradient(135deg,rgba(66,106,143,0.55) 0%, rgba(174,197,208,0.55) 50%, rgba(178,198,207,0.55) 100%);
    border-color: rgba(65, 102, 140, 0.5);
}

.profile-header {
    height: 200px;
    position: relative;
    overflow: visible;
}

.profile-bg-wrapper {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    overflow: hidden;
}

.profile-bg {
    position: absolute;
    top: -20px;
    left: -20px;
    right: -20px;
    bottom: -20px;
    background-image: url('https://cdn.jsdelivr.net/gh/Li-Huang-1/my-blog-images@main/img/image.jpg');
    background-size: cover;
    background-position: center;
    filter: blur(12px);
    -webkit-filter: blur(12px);
    transform: scale(1.1);
}

.avatar-container {
    position: absolute;
    top: 75px;
    left: 50%;
    transform: translateX(-50%);
    width: 150px;
    height: 150px;
    border-radius: 50%;
    overflow: hidden;
    border: 3px solid #ffffff;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
    z-index: 100;
}

.avatar {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.profile-content {
    padding: 20px;
    margin-top: 8px;
    padding-top: 44px;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 8px;
}

.username {
    font-family: 'Noto Serif SC', 'Roboto', 'Georgia', serif;
    font-size: 1.75rem;
    font-weight: 700;
    color: #333;
}

.location {
    font-family: 'Noto Serif SC', 'Roboto', 'Georgia', serif;
    font-size: 0.9rem;
    font-weight: 400;
    color: #666;
}

.intro-text {
    font-family: 'Noto Serif SC', 'Roboto', 'Georgia', serif;
    font-size: 1rem;
    font-weight: 400;
    color: #555;
    line-height: 1.6;
    padding-top: 8px;
}

.contact-info {
    display: flex;
    flex-direction: column;
    gap: 8px;
    padding: 12px;
    background-color: #f8f9fa;
    border-radius: 8px;
    margin-bottom: 12px;
}

.contact-item {
    display: flex;
    align-items: center;
    gap: 10px;
}

.contact-icon {
    font-size: 1rem;
    width: 20px;
    text-align: center;
}

.contact-text {
    font-size: 0.8125rem;
    color: #333333;
}

.logos-container {
    display: flex;
    justify-content: space-around;
    align-items: center;
    gap: 8px;
    padding: 16px 0 0;
    margin-top: 8px;
    width: 100%;
}

.logo-item {
    cursor: pointer;
    display: flex;
    justify-content: center;
    align-items: center;
    flex: 1;
}

.logo-item a {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
}

.logo-item img {
    max-width: 100%;
    height: auto;
    max-height: 48px;
    object-fit: contain;
    transition: transform 0.2s ease-in-out;
}

.logo-item img:hover {
    transform: scale(1.1);
}

.social-links-container {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 16px;
    padding: 12px 0;
    width: 100%;
}

.social-link {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background: rgba(107, 105, 105, 0.05);
    transition: all 0.3s ease;
    position: relative;
}

.social-link img {
    width: 70%;
    height: 70%;
    object-fit: contain;
    transition: transform 0.3s ease;
}

.social-link:hover {
    background: rgba(0, 0, 0, 0.1);
    transform: scale(1.1);
}

.social-link:hover img {
    transform: scale(1.15);
}

.qq-tooltip {
    position: absolute;
    bottom: -23px;
    left: 50%;
    transform: translateX(-50%);
    color: #000000;
    padding: 6px 12px;
    border-radius: 6px;
    font-size: 0.8rem;
    white-space: nowrap;
    /* 隐藏提示框 */
    opacity: 0;
    pointer-events: none;
    transition: opacity 0.3s ease;
    z-index: 1000;
}

.social-link-qq:hover .qq-tooltip {
    opacity: 1;
}

.stats-container {
    display: flex;
    justify-content: space-around;
    align-items: center;
    gap: 8px;
    padding: 16px 0 16px;
    margin-top: 0;
    border-top: 1px solid rgba(0, 0, 0, 0.06);
    width: 100%;
}

.stat-item {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 4px;
    flex: 1;
}

.stat-value {
    font-family: 'Noto Serif SC', 'Georgia', 'Times New Roman', serif;
    font-size: 1.5rem;
    font-weight: 600;
    color: #333;
    line-height: 1.2;
}

.stat-label {
    font-family: 'Noto Serif SC', 'Roboto', 'Georgia', serif;
    font-size: 0.8125rem;
    color: #888;
    letter-spacing: 0.05em;
}

.card-stack {
    display: flex;
    flex-direction: column;
    gap: 16px;
    width: 100%;
    max-width: 280px;
}

.card-gallery {
    width: 100%;
    height: 350px;
    overflow: hidden;
}

.gallery-container {
    width: 100%;
    height: 100%;
    position: relative;
}

.gallery-image-wrapper {
    width: 100%;
    height: 100%;
    position: relative;
    overflow: hidden;
}

.gallery-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.5s ease;
}

.gallery-image-wrapper:hover .gallery-image {
    transform: scale(1.1);
}

.gallery-overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 16px;
    background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
    opacity: 0;
    transition: opacity 0.3s ease;
}

.gallery-image-wrapper:hover .gallery-overlay {
    opacity: 1;
}

.gallery-title {
    font-family: 'Noto Serif SC', 'Roboto', 'Georgia', serif;
    font-size: 1rem;
    font-weight: 500;
    color: #ffffff;
    text-shadow: 0 1px 3px rgba(0, 0, 0, 0.5);
}

.gallery-placeholder {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background: linear-gradient(135deg, rgba(77, 84, 110, 0.3) 0%, rgba(100, 110, 140, 0.3) 100%);
}

.placeholder-text {
    font-family: 'Noto Serif SC', 'Roboto', 'Georgia', serif;
    font-size: 0.875rem;
    color: rgba(255, 255, 255, 0.6);
}

.card-clock {
    width: 100%;
    height: 250px;
    background: linear-gradient(to bottom, rgba(55,67,94,0.5) 0%, rgba(90,105,130,0.5) 20%, rgba(84,111,141,0.5) 50%, rgba(144,164,181,0.5) 75%, rgba(144,164,181,0.5) 100%);
    border-color: rgba(65, 102, 140, 0.5);
}

.waves-container {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 200px;
    overflow: hidden;
    mask-image: linear-gradient(to bottom, 
        transparent 0%,
        rgba(0,0,0,0.3) 30%,
        rgba(0,0,0,0.7) 60%,
        black 100%
    );
    -webkit-mask-image: linear-gradient(to bottom, 
        transparent 0%,
        rgba(0,0,0,0.3) 30%,
        rgba(0,0,0,0.7) 60%,
        black 100%
    );
}

.wave {
    position: absolute;
    bottom: 0;
    width: 200%;
    height: 100%;
    background-repeat: repeat-x;
    background-position: 0 0;
    animation-timing-function: linear;
    animation-iteration-count: infinite;
    animation-fill-mode: forwards;
    will-change: transform;
    backface-visibility: hidden;
    transform-style: preserve-3d;
    image-rendering: optimizeSpeed;
    transform: translateZ(0);
}

.wave1 {
    background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 800 150' preserveAspectRatio='none'%3E%3Cdefs%3E%3ClinearGradient id='waveGrad1' x1='0%25' y1='0%25' x2='0%25' y2='100%25'%3E%3Cstop offset='0%25' style='stop-color:rgba(77,84,110,0.6);stop-opacity:1'/%3E%3Cstop offset='100%25' style='stop-color:rgba(77,84,110,0.1);stop-opacity:1'/%3E%3C/linearGradient%3E%3C/defs%3E%3Cpath d='M0,75 Q100,20 200,75 T400,75 T600,75 T800,75 V150 H0 Z' fill='url(%23waveGrad1)'/%3E%3C/svg%3E");
    background-size: 50% 100%;
    animation-name: waveAnimation1;
    animation-duration: 50s;
    z-index: 1;
}

.wave2 {
    background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 600 150' preserveAspectRatio='none'%3E%3Cdefs%3E%3ClinearGradient id='waveGrad2' x1='0%25' y1='0%25' x2='0%25' y2='100%25'%3E%3Cstop offset='0%25' style='stop-color:rgba(100,100,130,0.5);stop-opacity:1'/%3E%3Cstop offset='100%25' style='stop-color:rgba(100,100,130,0.05);stop-opacity:1'/%3E%3C/linearGradient%3E%3C/defs%3E%3Cpath d='M0,75 Q75,30 150,75 T300,75 T450,75 T600,75 V150 H0 Z' fill='url(%23waveGrad2)'/%3E%3C/svg%3E");
    background-size: 50% 100%;
    animation-name: waveAnimation2;
    animation-duration: 32s;
    z-index: 2;
}

@keyframes waveAnimation1 {
    0% {
        transform: translateX(0);
    }
    100% {
        transform: translateX(-50%);
    }
}

@keyframes waveAnimation2 {
    0% {
        transform: translateX(0);
    }
    100% {
        transform: translateX(-50%);
    }
}

@media (max-width: 768px) {
    .cards-flex-row {
        flex-direction: column;
        align-items: center;
    }

    .cards-grid {
        grid-template-columns: 1fr;
        gap: 20px;
    }

    .card-column-wrapper {
        max-width: 100%;
    }

    .card-large {
        max-width: 100%;
        height: auto;
        min-height: 200px;
    }

    .player-content {
        flex-direction: column;
        text-align: center;
        gap: 16px;
    }

    .cover-wrapper {
        width: 100px;
        height: 100px;
    }

    .song-title {
        font-size: 1.25rem;
    }

    .controls {
        justify-content: center;
    }

    .card-tall {
        max-width: 100%;
        height: auto;
        min-height: 300px;
    }

    .profile-card {
        height: auto;
    }

    .profile-header {
        height: 180px;
        overflow: visible;
    }

    .avatar-container {
        top: 65px;
        left: 50%;
        transform: translateX(-50%);
        width: 130px;
        height: 130px;
        border-width: 2px;
    }

    .profile-content {
        padding: 16px;
        margin-top: 0;
        padding-top: 40px;
    }

    .social-links-container {
        gap: 12px;
    }

    .social-link {
        width: 30px;
        height: 30px;
    }

    .username {
        font-size: 1.25rem;
    }

    .location, .intro-text {
        font-size: 0.875rem;
    }

    .contact-text {
        font-size: 0.75rem;
    }

    .logo-item img {
        max-height: 30px;
    }

    .card-stack {
        max-width: 100%;
    }

    .card-placeholder {
        height: auto;
        min-height: 200px;
    }

    .card-clock {
        height: auto;
        min-height: 150px;
    }
}

@media (max-width: 480px) {
    .bottom-container {
        padding: 20px 16px;
    }

    .card-content {
        padding: 16px;
    }

    .card h3 {
        font-size: 1.25rem;
    }

    .card p {
        font-size: 0.875rem;
    }
}

.footer-card {
    position: fixed;
    bottom: -150px;
    left: 50%;
    transform: translateX(-50%);
    width: 850px;
    background: linear-gradient(to bottom, rgba(46, 58, 87, 0.95) 0%, rgba(54, 72, 104, 0.98) 100%);
    border: 1px solid rgba(65, 102, 140, 0.5);
    border-radius: 20px;
    padding: 20px;
    transition: bottom 0.5s ease-in-out;
    z-index: 100;
    box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.3);
}

.footer-card.show {
    bottom: 20px;
}

.footer-card-content {
    text-align: center;
}

.footer-card-title {
    font-size: 1rem;
    font-weight: 600;
    color: #B7C9D1;
    margin-bottom: 12px;
}

.footer-card-records {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 20px;
    margin-bottom: 12px;
}

.footer-card-info {
    font-size: 0.875rem;
    color: #8FA3B8;
}

.record-divider {
    color: #6B8CAE;
    font-size: 0.875rem;
}

.record-label {
    color: #6B8CAE;
}

.record-value {
    color: #B7C9D1;
}

.footer-card-copyright {
    font-size: 0.75rem;
    color: #6B8CAE;
    margin-top: 8px;
    padding-top: 12px;
    border-top: 1px solid rgba(65, 102, 140, 0.3);
}
</style>