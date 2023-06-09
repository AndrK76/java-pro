let stompClient = null;
let currentSessionId = '';
let roomId = '';
let isSpecialRoom = false;

const chatLineElementId = "chatLine";
const roomIdElementId = "roomId";
const messageElementId = "message";
const sendAreaId = "sendLine";
const specialAreaId = "specialRoomAreal";
const specialLabelId = "specialLabel";


const setConnected = (connected) => {
    const specialArea = document.getElementById(specialAreaId);
    const sendArea = document.getElementById(sendAreaId);
    const roomIdElement = document.getElementById(roomIdElementId);
    const connectBtn = document.getElementById("connect");
    const disconnectBtn = document.getElementById("disconnect");
    const chatLine = document.getElementById(chatLineElementId);

    if (!connected) {
        isSpecialRoom = false;
        specialArea.hidden = true;
        sendArea.hidden = true;
        chatLine.innerHTML = '';
    }

    connectBtn.hidden = connected;
    disconnectBtn.hidden = !connected;
    roomIdElement.disabled = connected;
    sendArea.hidden = !connected || isSpecialRoom;
    chatLine.hidden = !connected;
}

const connect = () => {
    const roomIdElement = document.getElementById(roomIdElementId);
    const defaultRoom = 1;
    roomId = roomIdElement.value;
    if (roomId == null || roomId == '') {
        if (!confirm("Connect to default room " + defaultRoom + "?")) {
            return;
        } else {
            roomId = defaultRoom;
            roomIdElement.value = roomId;
        }
    }
    let socket = new SockJS('/gs-guide-websocket', [], {
        sessionId: () => {
            currentSessionId = generate_SessionId();
            return currentSessionId;
        }
    });
    stompClient = Stomp.over(socket);
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log(`Connected to roomId: ${roomId} frame:${frame}`);
        stompClient.subscribe(`/topic/response.${roomId}`,
            (message) => showMessage(
                JSON.parse(message.body).messageStr,
                JSON.parse(message.body).clientId,
                JSON.parse(message.body).specialRoom
            )
        );
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

const sendMsg = () => {
    const messageElement = document.getElementById(messageElementId);
    const message = messageElement.value;
    stompClient.send(`/app/message.${roomId}`, {}, JSON.stringify({'messageStr': message}))
    messageElement.value = '';
}

const showMessage = (message, clientId, specialRoom) => {
    console.log('msg: ' + message + ', id: ' + clientId + ', IsSpecial: ' + specialRoom);
    if (specialRoom) {
        isSpecialRoom = true;
        const specialArea = document.getElementById(specialAreaId);
        const specialLabel = document.getElementById(specialLabelId);
        const sendArea = document.getElementById(sendAreaId);
        sendArea.hidden = true;
        specialArea.hidden = false;
        specialLabel.innerHTML = message;
    } else if (clientId == '' || clientId == currentSessionId) {
        const chatLine = document.getElementById(chatLineElementId);
        let newRow = chatLine.insertRow(-1);
        let newCell = newRow.insertCell(0);
        let newText = document.createTextNode(message);
        newCell.appendChild(newText);
    }
}

function generate_SessionId() {
    return Math.random().toString(36).substring(2, 15) +
        Math.random().toString(36).substring(2, 15);
}
