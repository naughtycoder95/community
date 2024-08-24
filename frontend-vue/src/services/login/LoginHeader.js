export default function LoginHeader() {
    let member = JSON.parse(localStorage.getItem("member"));

    if (member && member.accessToken) {
        return { Authorization: "Bearer " + member.accessToken };
    } else {
        return {};
      }
    }