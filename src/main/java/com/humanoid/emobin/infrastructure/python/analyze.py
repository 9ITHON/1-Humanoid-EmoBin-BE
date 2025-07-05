# analyze.py
import sys
import json
from analyze_core import preprocess, gpt_analyze_emotion_and_causes, parse_result

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print(json.dumps({"error": "No input text provided"}))
        sys.exit(1)

    text = sys.argv[1]
    cleaned = preprocess(text)
    result_text = gpt_analyze_emotion_and_causes(cleaned)
    result_json = parse_result(result_text)
    print(json.dumps(result_json, ensure_ascii=False))
