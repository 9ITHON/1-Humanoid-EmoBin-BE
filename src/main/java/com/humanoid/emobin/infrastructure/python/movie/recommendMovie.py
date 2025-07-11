import sys
import os
import re
from openai import OpenAI
from dotenv import load_dotenv

# ✅ .env에서 환경 변수 로드
load_dotenv()
client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))

def get_titles_from_gpt(emotion, message, genre, count):
    prompt = f"""
    You are a movie recommendation expert.

    Based on the following user's emotional state, motivational message, and genre preference,
    please recommend {count} emotionally fitting movies.

    - Emotion: {emotion}
    - Message: {message}
    - Preferred Genre: {genre if genre else 'Any'}

    Please respond with a numbered list of movie titles in English only, like:

    1. Inside Out
    2. The Pursuit of Happyness
    3. Soul
    ...
    """

    response = client.chat.completions.create(
        model="gpt-4",
        messages=[{"role": "user", "content": prompt}],
        temperature=0.7
    )

    content = response.choices[0].message.content
    lines = content.strip().split('\n')

    titles = []
    for line in lines:
        match = re.match(r'^\d+[\.\)]\s*(.+)', line)
        if match:
            titles.append(match.group(1).strip())

    return titles[:count]

if __name__ == "__main__":
    emotion = sys.argv[1] if len(sys.argv) > 1 else ""
    message = sys.argv[2] if len(sys.argv) > 2 else ""
    genre = sys.argv[3] if len(sys.argv) > 3 else ""
    count = int(sys.argv[4]) if len(sys.argv) > 4 else 3

    titles = get_titles_from_gpt(emotion, message, genre, count)

    for title in titles:
        print(title)
