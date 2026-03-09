def split_into_blocks(text, block_size):
    # Разделение текста на блоки фиксированного размера
    return [text[i:i + block_size] for i in range(0, len(text), block_size)]


def pad_block(block, block_size):
    # Дополнение блока до нужного размера
    return block.ljust(block_size, '\0')


def xor(a, b):
    # Операция XOR для двух строк
    return ''.join(chr(ord(x) ^ ord(y)) for x, y in zip(a, b))


def feistel_round(left, right, key):
    # Один раунд сети Фейштеля
    new_left = right
    new_right = xor(left, xor(right, key))
    return new_left, new_right


def feistel_network(block, key, rounds, encrypt=True):
    # Сеть Фейштеля для шифрования/дешифрования блока
    block_size = len(block)
    left, right = block[:block_size // 2], block[block_size // 2:]

    for i in range(rounds):
        if encrypt:
            left, right = feistel_round(left, right, key)
        else:
            right, left = feistel_round(right, left, key)

    return left + right


def encrypt(text, key, rounds=10):
    # Шифрование текста
    block_size = 8  # Размер блока в символах
    blocks = split_into_blocks(text, block_size)
    encrypted_blocks = []

    for block in blocks:
        block = pad_block(block, block_size)
        encrypted_block = feistel_network(block, key, rounds, encrypt=True)
        encrypted_blocks.append(encrypted_block)

    return ''.join(encrypted_blocks)


def decrypt(ciphertext, key, rounds=10):
    # Дешифрование текста
    block_size = 8  # Размер блока в символах
    blocks = split_into_blocks(ciphertext, block_size)
    decrypted_blocks = []

    for block in blocks:
        decrypted_block = feistel_network(block, key, rounds, encrypt=False)
        decrypted_blocks.append(decrypted_block)

    return ''.join(decrypted_blocks).rstrip('\0')


# Основная программа
if name == "main":
    # Ввод данных от пользователя
    plaintext = input("Введите текст для шифрования: ")
    key = input("Введите ключ: ")
    rounds = int(input("Введите количество раундов (по умолчанию 10): ") or 10)

    # Шифрование
    ciphertext = encrypt(plaintext, key, rounds)
    print(f"\nЗашифрованный текст: {ciphertext}")

    # Дешифрование
    decrypted_text = decrypt(ciphertext, key, rounds)
    print(f"Дешифрованный текст: {decrypted_text}")

    # Сравнение исходного и дешифрованного текста
    if plaintext == decrypted_text:
        print("Тексты совпадают!")
    else:
        print("Тексты не совпадают!")