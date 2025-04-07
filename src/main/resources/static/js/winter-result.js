const searchQuery = '겨울 쿨톤 메이크업';
const maxResults = 6;

function loadYouTubeVideos() {
    const apiUrl = `https://www.googleapis.com/youtube/v3/search?part=snippet&q=${encodeURIComponent(searchQuery)}&key=${YOUTUBE_API_KEY}&type=video&maxResults=${maxResults}`;


    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            const videoContainer = document.getElementById('video-container');
            data.items.forEach(item => {
                const videoId = item.id.videoId;
                const videoStatusUrl = `https://www.googleapis.com/youtube/v3/videos?id=${videoId}&part=status&key=${YOUTUBE_API_KEY}`;

                // Fetch video status
                fetch(videoStatusUrl)
                    .then(response => response.json())
                    .then(statusData => {
                        const embeddable = statusData.items[0].status.embeddable;
                        if (embeddable) {
                            // Create iframe only if embeddable
                            const videoElement = document.createElement('iframe');
                            videoElement.src = `https://www.youtube.com/embed/${videoId}`;
                            videoElement.width = '300';
                            videoElement.height = '200';
                            videoElement.frameBorder = '0';
                            videoElement.allow = 'accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture';
                            videoElement.allowFullscreen = true;

                            videoContainer.appendChild(videoElement);
                        }
                    })
                    .catch(error => console.error('Error fetching video status:', error));
            });
        })
        .catch(error => console.error('Error fetching YouTube videos:', error));
}

document.addEventListener('DOMContentLoaded', loadYouTubeVideos);